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

  def addItem(data: TodoItemData): IO[String] = {
    logger.info(s"Try to add TodoItem with data: $data")
    for {
      newId <- TodoItem.generateId
      newItem = TodoItem(newId, data)
      _ <- IO {
        todoItems += newItem
      }
    } yield newId
  }

  def updateItem(updatedItem: TodoItem): IO[Unit] = {
    logger.info(s"Update item with id = ${updatedItem.id} as $updatedItem")
    for {
      // TODO: Add validate update unknown item
      _ <- IO {
        todoItems --= todoItems.find(_.id == updatedItem.id).toSeq
      }
      _ <- IO {
        todoItems += updatedItem
      }
    } yield ()
  }

  def deleteItem(id: String): IO[Option[String]] = {
    logger.info("Delete item with id = $id")
    for {
      itemOpt <- IO(todoItems.find(_.id == id))
      _ <- IO(todoItems.filterNot(item => itemOpt.map(_.id).contains(item.id)))
    } yield {
      itemOpt.map(_.id)
    }
  }
}

object TodoRepository {

  def empty: IO[TodoRepository] = IO(new TodoRepository(ListBuffer()))
}
