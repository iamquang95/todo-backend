// Copyright (C) 2014-2018 Anduin Transactions Inc.

package iamquang95.todo

import scala.collection.mutable.ListBuffer

import cats.effect.IO
import com.typesafe.scalalogging.Logger
import iamquang95.todo.model.{TodoItem, TodoItemData}

final class TodoRepository(
  private val todoItems: ListBuffer[TodoItem]
) {

  private val logger = Logger(classOf[TodoRepository])

  def getAll: IO[Seq[TodoItem]] = {
    logger.info("Get all todoItems")
    IO(todoItems)
  }

  def getItem(id: String): IO[Option[TodoItem]] = {
    logger.info(s"Get item with id: $id")
    IO {
      todoItems.find(_.id == id)
    }
  }

  def addItem(data: TodoItemData): IO[TodoItem] = {
    logger.info(s"Try to add TodoItem with data: $data")
    for {
      newId <- TodoItem.generateId
      newItem = TodoItem.fromTodoItemData(newId, data)
      _ <- IO {
        todoItems += newItem
      }
    } yield newItem
  }

  def updateItem(id: String, data: TodoItemData): IO[TodoItem] = {
    logger.info(s"Update item with id = $id as $data")
    for {
      // TODO: Add validate update unknown item
      _ <- IO {
        todoItems --= todoItems.find(_.id == id).toSeq
      }
      updatedItem = TodoItem.fromTodoItemData(id, data)
      _ <- IO {
        todoItems += updatedItem
      }
    } yield updatedItem
  }

  def deleteItem(id: String): IO[Option[TodoItem]] = {
    logger.info("Delete item with id = $id")
    for {
      itemOpt <- IO(todoItems.find(_.id == id))
      _ <- IO(todoItems --= todoItems.filter(item => itemOpt.map(_.id).contains(item.id)))
    } yield {
      itemOpt
    }
  }

  def deleteAll(): IO[Unit] = {
    logger.info("Delete all items")
    for {
      _ <- IO(todoItems --= todoItems)
    } yield ()
  }
}

object TodoRepository {

  def empty: IO[TodoRepository] = IO(new TodoRepository(ListBuffer()))
}
