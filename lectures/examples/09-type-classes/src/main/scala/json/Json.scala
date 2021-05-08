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
  def toJson(a: A): JsonValue
}

object JsonSerializable {
  def toJson[A](a: A)(implicit js: JsonSerializable[A]): JsonValue = js.toJson(a)
  def toString[A : JsonSerializable](a: A): String = JsonValue.toString(toJson(a))

  object ops {
    implicit class JsonSerializableOps[A : JsonSerializable](a: A) {
      def toJson: JsonValue = JsonSerializable.toJson(a)
      def toJsonString: String = JsonSerializable.toString(a)
    }
  }

  implicit val intSerializable = new JsonSerializable[Int] {
    def toJson(a: Int): JsonValue = JsonNumber(a)
  }

  implicit val stringSerializable = new JsonSerializable[String] {
    def toJson(a: String): JsonValue = JsonString(a)
  }

  implicit val booleanSerializable = new JsonSerializable[Boolean] {
    def toJson(a: Boolean): JsonValue = JsonBoolean(a)
  }

  implicit def optionSerializable[A : JsonSerializable] = new JsonSerializable[Option[A]] {
    def toJson(opt: Option[A]): JsonValue = opt match {
      case Some(a) => JsonSerializable.toJson(a)
      case _ => JsonNull
    }
  }

  implicit def listSerializable[A : JsonSerializable] = new JsonSerializable[List[A]] {
    def toJson(a: List[A]): JsonValue = JsonArray(
      a.map(value => JsonSerializable.toJson(value))
    )
  }
}
