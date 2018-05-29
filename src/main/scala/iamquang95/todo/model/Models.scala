// Copyright (C) 2014-2018 Anduin Transactions Inc.

package iamquang95.todo.model

import scala.util.Random

import cats.effect.IO

case class TodoItemData(
  title: String = "",
  completed: Boolean = false,
  order: Int = 0
)

case class TodoItem(
  id: String,
  title: String,
  completed: Boolean,
  order: Int
)

object TodoItem {

  def generateId: IO[String] =
    IO(Random.nextInt(Integer.MAX_VALUE).toString)

  def fromTodoItemData(
    id: String,
    data: TodoItemData
  ): TodoItem = TodoItem(
    id = id,
    title = data.title,
    completed = data.completed,
    order = data.order
  )
}
