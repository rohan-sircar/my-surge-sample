package com.example.model

import surge.scaladsl.command.AggregateCommandModel

import scala.util.{Failure, Success, Try}
import com.example.account.Book
import com.example.command._
import com.example.event._
import com.example.exception._
import org.slf4j.{Logger, LoggerFactory}
import java.util.UUID
import org.graalvm.polyglot.Context
import play.api.libs.json.Json
import play.api.libs.json.JsValue

final case class State(aggregateId: String, value: JsValue)
object State {
  implicit val format = Json.format[State]
}
final case class Command(aggregateId: String, value: JsValue)
object Command {
  implicit val format = Json.format[Command]
}
final case class Event(aggregateId: String, value: JsValue)
object Event {
  implicit val format = Json.format[Event]
}

final class JsLibraryCommandModel(ctx: Context)
    extends AggregateCommandModel[State, Command, Event] {
  val log: Logger = LoggerFactory.getLogger(getClass)

  val processCommandScript =
    os.read(os.home / "surge" / "my-js-app" / "commandHandler.js")
  val processEventScript =
    os.read(os.home / "surge" / "my-js-app" / "eventHandler.js")

  override def processCommand(
      aggregate: Option[State],
      command: Command
  ): Try[List[Event]] = {
    log.info("Processing Command ...")

    val result = ctx
      .eval("js", processCommandScript)
      .execute(
        // Json.stringify(Json.toJson(aggregate)),
        // Json.stringify(Json.toJson(command))
        aggregate.getOrElse(null),
        command
      )

    // println(s"Result = $result")

    val r = result.asString()
    val ev = Try(Json.parse(r).as[List[Event]])

    // println(s"Ev = $ev")

    // ev match {
    //   case Some(value) => Success(value)
    //   case None        => Failure(new Exception("error occured"))
    // }

    // Success(List(Event("ghi", "foo")))
    ev
  }

  override def handleEvent(
      aggregate: Option[State],
      event: Event
  ): Option[State] = {
    log.info("Processing Event ...")

    // "received event $event"

    val result = ctx
      .eval("js", processEventScript)
      .execute(aggregate.getOrElse(null), event)

    val r = result.asString()
    val state = Json.parse(r).asOpt[State]
    log.info(s"state = $state")
    state

  }

}
