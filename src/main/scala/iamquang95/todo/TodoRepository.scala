// Copyright (C) 2014-2018 Anduin Transactions Inc.

package iamquang95.todo

import scala.collection.mutable.ListBuffer

import cats.effect.IO
import iamquang95.todo.model.{TodoItem, TodoItemData}

final class TodoRepository(
  private val todoItems: ListBuffer[TodoItem]
) {

  def getItem(id: String): IO[Option[TodoItem]] = {
    IO {
      todoItems.find(_.id == id)
    }
  }

  def addItem(data: TodoItemData): IO[String] = {
    for {
      newId <- TodoItem.generateId
      newItem = TodoItem(newId, data)
      _ <- IO {
        todoItems += newItem
      }
    } yield newId
  }

  def updateItem(updatedItem: TodoItem): IO[Unit] = {
    for {
      // TODO: Add validate update unknown item
      _ <- IO {
        todoItems.filterNot(_.id == updatedItem.id)
      }
      _ <- IO {
        todoItems += updatedItem
      }
    } yield ()
  }

  def deleteItem(id: String): IO[Unit] = {
    val item =  todoItems.find(_.id == id)
    IO(item.fold(throw new RuntimeException("Delete unknown item"))(_ => todoItems.filterNot(_.id == id)))
  }
}

object TodoRepository {

  def empty: IO[TodoRepository] = IO(new TodoRepository(ListBuffer()))
}
