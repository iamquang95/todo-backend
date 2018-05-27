// Copyright (C) 2014-2018 Anduin Transactions Inc.

package iamquang95.todo

import cats.effect.IO
import fs2.StreamApp
import iamquang95.todo.model.{TodoItem, TodoItemData}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder
import scala.concurrent.ExecutionContext.Implicits.global

import org.http4s.{EntityDecoder, EntityEncoder, HttpService}
import org.http4s.circe._
import io.circe.generic.auto._
import org.http4s.server.middleware.CORS

object TodoServer extends StreamApp[IO] with Http4sDsl[IO] {

  implicit val todoItemDataDecoder: EntityDecoder[IO, TodoItemData] =
    jsonOf[IO, TodoItemData]
  implicit val todoItemDecoder: EntityDecoder[IO, TodoItem] =
    jsonOf[IO, TodoItem]
  implicit val todoItemSeqDecoder: EntityDecoder[IO, Seq[TodoItem]] =
    jsonOf[IO, Seq[TodoItem]]

  implicit val todoItemDataEncoder: EntityEncoder[IO, TodoItemData] =
    jsonEncoderOf[IO, TodoItemData]
  implicit val todoItemEncoder: EntityEncoder[IO, TodoItem] =
    jsonEncoderOf[IO, TodoItem]
  implicit val todoItemSeqEncoder: EntityEncoder[IO, Seq[TodoItem]] =
    jsonEncoderOf[IO, Seq[TodoItem]]

  private val todoRepo = TodoRepository.empty.unsafeRunSync()

  private val APIUrl = "todo"

  private val serviceRoutes = HttpService[IO] {

    case GET -> Root / APIUrl =>
      todoRepo.getAll.flatMap(Ok(_))

    case GET -> Root / APIUrl / todoId =>
      todoRepo
        .getItem(todoId)
        .flatMap(_.fold(NotFound())(Ok(_)))

    case req @ POST -> Root / APIUrl =>
      req.as[TodoItemData].flatMap(todoRepo.addItem).flatMap(Created(_))

    case req @ PUT -> Root / APIUrl =>
      req.as[TodoItem].flatMap(todoRepo.updateItem).flatMap(Ok(_))

    case DELETE -> Root / APIUrl / todoId =>
      todoRepo.deleteItem(todoId).flatMap(_.fold(NotFound())(_ => NoContent()))
  }

  def stream(args: List[String], requestShutdown: IO[Unit]) = {

    val port: Int = sys.env.getOrElse("PORT", "8080").toInt

    val service: HttpService[IO] = CORS[IO](serviceRoutes)

    BlazeBuilder[IO]
      .bindHttp(port, "0.0.0.0")
      .mountService(service, "/")
      .serve
  }
}
