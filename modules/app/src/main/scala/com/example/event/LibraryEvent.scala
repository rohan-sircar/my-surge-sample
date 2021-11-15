// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example.event

import play.api.libs.json.{Format, JsValue, Json}

import java.util.UUID

sealed trait LibraryEvent {
  def id: UUID
  def toJson: JsValue
}

object BookCreated {
  implicit val format: Format[BookCreated] = Json.format
}
case class BookCreated(
    id: UUID,
    authorName: String,
    title: String
) extends LibraryEvent {
  override def toJson: JsValue = Json.toJson(this)
}

// object BookUpdated {
//   implicit val format: Format[BookUpdated] = Json.format
// }
// case class BookUpdated(accountNumber: UUID, newBalance: Double)
//     extends LibraryEvent {
//   override def toJson: JsValue = Json.toJson(this)
// }

object BookDeleted {
  implicit val format: Format[BookDeleted] = Json.format
}
case class BookDeleted(id: UUID) extends LibraryEvent {
  override def toJson: JsValue = Json.toJson(this)
}
