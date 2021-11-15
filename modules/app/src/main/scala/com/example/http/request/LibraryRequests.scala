// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example.http.request

import com.example.command.{CreateBook}

import java.util.UUID
import com.example.command.DeleteBook

case class CreateBookRequest(
    authorName: String,
    title: String
)
final case class DeleteBookRequest(bookId: UUID)
// case class CreditAccountRequest(accountNumber: UUID, amount: Double)
// case class DebitAccountRequest(accountNumber: UUID, amount: Double)

object RequestToCommand {
  def requestToCommand(request: CreateBookRequest): CreateBook = {

    CreateBook(
      UUID.randomUUID(),
      request.authorName,
      request.title
    )
  }

  def requestToCommand(request: DeleteBookRequest): DeleteBook = {

    DeleteBook(request.bookId)
  }

  // def requestToCommand(request: CreditAccountRequest): CreditAccount = {
  //   CreditAccount(request.accountNumber, request.amount)
  // }

  // def requestToCommand(request: DebitAccountRequest): DebitAccount = {
  //   DebitAccount(request.accountNumber, request.amount)
  // }
}
