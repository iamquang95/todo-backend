// Copyright (C) 2014-2018 Anduin Transactions Inc.

package iamquang95.todo

object TodoServerConfig {

  val BASE_URL = "https://iamquang95-todoapp.herokuapp.com/todo/"
  val PORT: Int = sys.env.getOrElse("PORT", "8080").toInt

}
