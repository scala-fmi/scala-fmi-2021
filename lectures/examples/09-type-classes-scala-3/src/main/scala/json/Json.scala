package json

import scala.language.implicitConversions

sealed trait JsonValue
case class JsonNumber(value: BigDecimal) extends JsonValue
case class JsonString(value: String) extends JsonValue
case class JsonBoolean(value: Boolean) extends JsonValue
case class JsonArray(value: Seq[JsonValue]) extends JsonValue
case class JsonObject(value: Map[String, JsonValue]) extends JsonValue
case object JsonNull extends JsonValue

object JsonValue {
  def toString(json: JsonValue): String = json match {
    case JsonNumber(value) => value.toString
    case JsonString(value) => s"""\"$value\""""
    case JsonBoolean(value) => value.toString
    case JsonArray(elements) => "[" + elements.map(toString).mkString(", ") + "]"
    case JsonObject(members) =>
      val membersStrings = members.map { case (key, value) =>
        s"""  \"$key\": ${toString(value)}"""
      }
      "{\n" + membersStrings.mkString(",\n") + "\n}"
    case JsonNull => "null"
  }
}

trait JsonSerializable[A] {
  extension (a: A) {
    def toJson: JsonValue
    def toJsonString = JsonValue.toString(a.toJson)
  }
}

object JsonSerializable {
  def toJson[A : JsonSerializable](a: A): JsonValue = a.toJson
  def toString[A : JsonSerializable](a: A): String = a.toJsonString

  object ops {
    implicit class JsonSerializableOps[A : JsonSerializable](a: A) {
      def toJson = JsonSerializable.toJson(a)
      def toJsonString = JsonSerializable.toString(a)
    }
  }

  given JsonSerializable[Int] with {
    extension (a: Int) def toJson: JsonValue = JsonNumber(a)
  }

  given JsonSerializable[String] with {
    extension (a: String) def toJson: JsonValue = JsonString(a)
  }

  given JsonSerializable[Boolean] with {
    extension (a: Boolean) def toJson: JsonValue = JsonBoolean(a)
  }

  given [A : JsonSerializable]: JsonSerializable[List[A]] with {
    extension (xs: List[A]) def toJson: JsonValue = JsonArray(
      xs.map(_.toJson)
    )
  }

  given [A : JsonSerializable]: JsonSerializable[Option[A]] with {
    extension (opt: Option[A]) def toJson: JsonValue = opt match {
      case Some(a) => JsonSerializable.toJson(a)
      case _ => JsonNull
    }
  }
}
