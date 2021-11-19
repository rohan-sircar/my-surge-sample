// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example.account

import play.api.libs.json.{Format, Json}

import java.util.UUID

object Book {
  implicit val format: Format[Book] = Json.format
}

final case class Book(
    id: UUID,
    authorName: String,
    title: String
)

//println
