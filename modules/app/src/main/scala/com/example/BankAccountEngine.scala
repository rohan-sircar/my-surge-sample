// Copyright © 2017-2021 UKG Inc. <https://www.ukg.com>

package com.example

import com.example.account.Book
import com.example.command.LibraryCommand
import com.example.event.LibraryEvent
import com.example.model.LibrarySurgeModel
import surge.scaladsl.command.SurgeCommand
import org.graalvm.polyglot.Context

import java.util.UUID
import com.example.model.State
import com.example.model.Command
import com.example.model.Event
import com.example.model.JsSurgeModel

final class LibraryEngine(ctx: Context) {
  lazy val surgeEngine
      : SurgeCommand[UUID, Book, LibraryCommand, Nothing, LibraryEvent] = {
    val engine = SurgeCommand(new LibrarySurgeModel(ctx))
    engine.start()
    engine
  }
}

final class JsEngine(ctx: Context) {
  lazy val surgeEngine: SurgeCommand[UUID, State, Command, Nothing, Event] = {
    val engine = SurgeCommand(new JsSurgeModel(ctx))
    engine.start()
    engine
  }
}
