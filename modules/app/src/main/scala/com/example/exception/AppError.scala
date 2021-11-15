// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example.exception

import java.util.UUID

sealed trait AppError extends Exception {
  def msg: String

  override def getMessage(): String = msg
}

final case class InsufficientFundsException(accountNumber: UUID)
    extends AppError {
  def msg: String =
    s"Insufficient Funds in account $accountNumber to complete this transaction"
}
final case class AccountDoesNotExistException(accountNumber: UUID)
    extends AppError {
  def msg: String = s"Account with id $accountNumber does not exist"
}
