// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example.model

import play.api.libs.json.Json
import surge.core.{
  SerializedAggregate,
  SerializedMessage,
  SurgeAggregateReadFormatting,
  SurgeAggregateWriteFormatting,
  SurgeEventWriteFormatting
}
import surge.kafka.KafkaTopic
import surge.scaladsl.command.{AggregateCommandModel, SurgeCommandBusinessLogic}
import com.example.account.Book
import com.example.command._
import com.example.event._

import java.util.UUID

import org.graalvm.polyglot.Context

final class JsSurgeModel(ctx: Context)
    extends SurgeCommandBusinessLogic[
      UUID,
      State,
      Command,
      Event
    ] {

  val cm = new JsLibraryCommandModel(ctx)
  def commandModel: AggregateCommandModel[
    State,
    Command,
    Event
  ] = cm

  def aggregateName: String = "library"

  def stateTopic: KafkaTopic = KafkaTopic("library-state")

  def eventsTopic: KafkaTopic = KafkaTopic("library-events")

  def aggregateReadFormatting: SurgeAggregateReadFormatting[State] =
    (bytes: Array[Byte]) => Json.parse(bytes).asOpt[State]

  def aggregateWriteFormatting: SurgeAggregateWriteFormatting[State] =
    (agg: State) => {
      val aggBytes = Json.toJson(agg).toString().getBytes()
      val messageHeaders = Map("aggregate_id" -> agg.aggregateId.toString)
      SerializedAggregate(aggBytes, messageHeaders)
    }

  def eventWriteFormatting: SurgeEventWriteFormatting[Event] =
    (evt: Event) => {
      val evtKey = evt.aggregateId.toString
      val evtBytes = Json.toBytes(Json.toJson(evt))
      val messageHeaders = Map("aggregate_id" -> evt.aggregateId.toString)
      SerializedMessage(evtKey, evtBytes, messageHeaders)
    }
}
