// Copyright (C) 2014-2018 Anduin Transactions Inc.

package iamquang95.todo.model

import java.util.UUID

import cats.effect.IO

case class TodoItemData(
  desc: String
)

case class TodoItem(
  id: String,
  data: TodoItemData
)

object TodoItem {
  def generateId: IO[String] =
    IO("tdi" + UUID.randomUUID().toString)
}
