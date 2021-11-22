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

final case class State(aggregateId: String, value: String)
object State {
  implicit val format = Json.format[State]
}
final case class Command(aggregateId: String, value: String)
object Command {
  implicit val format = Json.format[Command]
}
final case class Event(aggregateId: String, value: String)
object Event {
  implicit val format = Json.format[Event]
}

final class JsLibraryCommandModel(ctx: Context)
    extends AggregateCommandModel[State, Command, Event] {
  val log: Logger = LoggerFactory.getLogger(getClass)

  override def processCommand(
      aggregate: Option[State],
      command: Command
  ): Try[List[Event]] = {
    log.info("Processing Command ...")

    val result = ctx
      .eval(
        "js",
        """
          (function processCommand(aggregate, command) {
              console.log(aggregate, command)
              // console.log(command.id())
              //return "hello"
              let cmd = JSON.parse(command.value())
              console.log(cmd.method)
              console.log(cmd.data.title)
              console.log(cmd.data.author)
              return `[{"aggregateId":"abc","value":"bar"},{"aggregateId":"def","value":"baz"}]`
          })
        """
      )
      .execute(
        // Json.stringify(Json.toJson(aggregate)),
        // Json.stringify(Json.toJson(command))
        aggregate,
        command
      )

    // println(s"Result = $result")

    val r = result.asString()
    val ev = Json.parse(r).asOpt[List[Event]]

    // println(s"Ev = $ev")

    ev match {
      case Some(value) => Success(value)
      case None        => Failure(new Exception("error occured"))
    }

    // Success(List(Event("ghi", "foo")))
  }

  override def handleEvent(
      aggregate: Option[State],
      event: Event
  ): Option[State] = {
    println(s"received event $event")
    aggregate
  }

}
