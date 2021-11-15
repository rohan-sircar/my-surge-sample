// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example

import com.example.account.Book
import com.example.command.LibraryCommand
import com.example.event.LibraryEvent
import com.example.model.LibrarySurgeModel
import surge.scaladsl.command.SurgeCommand

import java.util.UUID

object LibraryEngine {
  lazy val surgeEngine
      : SurgeCommand[UUID, Book, LibraryCommand, Nothing, LibraryEvent] = {
    val engine = SurgeCommand(LibrarySurgeModel)
    engine.start()
    engine
  }
}
