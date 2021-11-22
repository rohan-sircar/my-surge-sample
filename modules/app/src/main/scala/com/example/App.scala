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
import java.util.UUID
import com.example.model.Command
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import scala.util.Try

object Boot extends App with PlayJsonSupport with LibraryRequestSerializer {

  val context = Context.newBuilder().allowAllAccess(true).build()

  val jsEngine = new JsEngine(context)

  implicit val system = jsEngine.surgeEngine.actorSystem
  private val log = LoggerFactory.getLogger(getClass)
  private val config = ConfigFactory.load()

  val route =
    pathPrefix("library") {
      pathPrefix("books") {
        concat(
          post {
            entity(as[JsValue]) { json =>
              println("reached here")
              val entityId =
                Try(UUID.fromString((json \ "data" \ "id").as[String]))
                  .getOrElse(UUID.randomUUID())
              // val entityId = UUID.randomUUID()
              val f = jsEngine.surgeEngine
                .aggregateFor(entityId)
                .sendCommand(
                  Command(
                    UUID.randomUUID().toString,
                    // """{"action":"CreateBook","data":{"title":"foo","author":"bar"}}"""
                    json
                  )
                )
                .flatMap {
                  case CommandSuccess(aggregateState) =>
                    Future.successful(aggregateState)
                  case CommandFailure(reason) => Future.failed(reason)
                }

              onSuccess(f) {
                case Some(value) => complete(value)
                case None        => complete(StatusCodes.BadRequest)
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
