// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example.model

import surge.scaladsl.command.AggregateCommandModel

import scala.util.{Failure, Success, Try}
import com.example.account.Book
import com.example.command._
import com.example.event._
import com.example.exception._
import org.slf4j.{Logger, LoggerFactory}
import java.util.UUID

object LibraryCommandModel
    extends AggregateCommandModel[Book, LibraryCommand, LibraryEvent] {
  val log: Logger = LoggerFactory.getLogger(getClass)

  override def processCommand(
      aggregate: Option[Book],
      command: LibraryCommand
  ): Try[List[LibraryEvent]] = {
    log.info("Processing Command ...")
    command match {
      case CreateBook(id, authorName, title) =>
        if (aggregate.isDefined) {
          // Aggregate already exists - no need to recreate
          Success(List.empty)
        } else {
          Success(
            List(
              BookCreated(
                id,
                authorName,
                title
              )
            )
          )
        }
      case DeleteBook(id) =>
        aggregate match {
          case Some(value) => Success(List(BookDeleted(id)))
          case None        => Failure(new Exception("Book does not exist"))
        }

    }
  }

  // case credit: CreditAccount =>
  //   aggregate
  //     .map { existing =>
  //       Success(
  //         List(
  //           BookUpdated(
  //             existing.accountNumber,
  //             existing.balance + credit.amount
  //           )
  //         )
  //       )
  //     }
  //     .getOrElse(
  //       Failure(new AccountDoesNotExistException(credit.accountNumber))
  //     )
  // case debit: DebitAccount =>
  //   aggregate
  //     .map { existing =>
  //       if (existing.balance >= debit.amount) {
  //         Success(
  //           List(
  //             BookUpdated(
  //               existing.accountNumber,
  //               existing.balance - debit.amount
  //             )
  //           )
  //         )
  //       } else {
  //         Failure(new InsufficientFundsException(existing.accountNumber))
  //       }
  //     }
  //     .getOrElse(
  //       Failure(new AccountDoesNotExistException(debit.accountNumber))
  //     )

  override def handleEvent(
      aggregate: Option[Book],
      event: LibraryEvent
  ): Option[Book] = {
    event match {
      case BookCreated(id, authorName, title) =>
        Some(Book(id, authorName, title))
      case BookDeleted(id) => None
      // case updated: BookUpdated =>
      //   aggregate.map(_.copy(balance = updated.newBalance))
    }
  }
}
