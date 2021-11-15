// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example.http.serializer

import com.example.http.request.{CreateBookRequest}
import play.api.libs.json.Json

trait LibraryRequestSerializer {
  implicit val createAccountFormat = Json.format[CreateBookRequest]
  // implicit val creditAccountFormat = Json.format[CreditAccountRequest]
  // implicit val debitAccountFormat = Json.format[DebitAccountRequest]
}
