// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import com.example.account.Book
import com.example.http.request.CreateBookRequest
import com.example.http.serializer.LibraryRequestSerializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.slf4j.{LoggerFactory, MDC}
import surge.scaladsl.common.{CommandFailure, CommandSuccess}
import com.example.http.request.RequestToCommand._
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.io.StdIn
import surge.internal.utils.MdcExecutionContext.mdcExecutionContext
import com.example.command.DeleteBook
import com.example.http.request.DeleteBookRequest
import org.graalvm.polyglot.Context

object Boot extends App with PlayJsonSupport with LibraryRequestSerializer {

  val context = Context.newBuilder().allowAllAccess(true).build()

  val engine = new LibraryEngine(context)

  implicit val system = engine.surgeEngine.actorSystem
  private val log = LoggerFactory.getLogger(getClass)
  private val config = ConfigFactory.load()

  val route =
    pathPrefix("library") {
      pathPrefix("books") {
        concat(
          post {
            entity(as[CreateBookRequest]) { request =>
              val createBookCommand = requestToCommand(request)
              MDC.put(
                "book_id",
                createBookCommand.id.toString
              )
              val createdBookF: Future[Option[Book]] =
                engine.surgeEngine
                  .aggregateFor(createBookCommand.id)
                  .sendCommand(createBookCommand)
                  .flatMap {
                    case CommandSuccess(aggregateState) =>
                      Future.successful(aggregateState)
                    case CommandFailure(reason) => Future.failed(reason)
                  }

              onSuccess(createdBookF) {
                case Some(book) => complete(book)
                case None       => complete(StatusCodes.InternalServerError)
              }

            }
          },
          path(JavaUUID) { uuid =>
            get {
              MDC.put("book_id", uuid.toString)
              val bookStateF =
                engine.surgeEngine.aggregateFor(uuid).getState
              log.info("Get book")
              onSuccess(bookStateF) {
                case Some(bookState) => complete(bookState)
                case None            => complete(StatusCodes.NotFound)
              }
            }
          },
          path(JavaUUID) { uuid =>
            delete {
              MDC.put("book_id", uuid.toString)
              val deleteBookCommand = requestToCommand(DeleteBookRequest(uuid))
              val bookStateF =
                engine.surgeEngine
                  .aggregateFor(uuid)
                  .sendCommand(deleteBookCommand)
                  .flatMap {
                    case CommandSuccess(aggregateState) =>
                      Future.successful(aggregateState)
                    case CommandFailure(reason) => Future.failed(reason)
                  }

              log.info("Get book")
              // onSuccess(bookStateF) {
              //   case Some(bookState) => complete(bookState)
              //   case None            => complete(StatusCodes.NotFound)
              // }
              onSuccess(bookStateF) { _ =>
                complete(StatusCodes.OK)
              }
            }
          }
        )
      }
    }

  val host = config.getString("http.host")
  val port = config.getInt("http.port")
  val bindingFuture = Http().newServerAt(host, port).bind(route)

  log.info(s"Server is running on  http://$host:$port")

}
