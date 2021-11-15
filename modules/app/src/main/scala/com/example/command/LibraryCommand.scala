// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example.command

import java.util.UUID

sealed trait LibraryCommand

case class CreateBook(
    id: UUID,
    authorName: String,
    title: String
) extends LibraryCommand

case class DeleteBook(id: UUID) extends LibraryCommand

// case class CreditAccount(accountNumber: UUID, amount: Double)
//     extends LibraryCommand
// case class DebitAccount(accountNumber: UUID, amount: Double)
//     extends LibraryCommand
