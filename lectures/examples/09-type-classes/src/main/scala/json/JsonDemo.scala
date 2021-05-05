package json

import json.JsonSerializable.ops._

object JsonDemo extends App {
  List(1, 2, 3).toJsonString // [1, 2, 3]

  val ivan = Person("Ivan", "ivan@abv.bg", 23)
  val georgi = Person("Georgi", "georgi@gmail.bg", 28)

  ivan.toJson
  ivan.toJsonString

  List(ivan, georgi).toJsonString
}
