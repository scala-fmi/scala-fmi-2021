package json.examples

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json, ParsingFailure}
import io.circe.parser._

case class Person(name: String, age: Int)
case class IdCard(id: String, person: Person)

object IdCard {
  implicit val idCardEncoder: Encoder[IdCard] = new Encoder[IdCard] {
    override def apply(a: IdCard): Json = Json.obj(
      "id" -> a.id.asJson,
      "person" -> a.person.asJson
    )
  }


  implicit val idCardDecoder: Decoder[IdCard] = (c: HCursor) => for {
    id <- c.get[String]("id")
    person <- c.get[Person]("person")
  } yield IdCard(id, person)

}


object Person {
  implicit val personEncoder: Encoder[Person] = (a: Person) => Json.obj(
    "name" -> a.name.asJson,
    "age" -> a.age.asJson
  )

    implicit val personDecoder: Decoder[Person] = (c: HCursor) => for {
      name <- c.get[String]("name")
      age <- c.get[Int]("age")
    } yield Person(name, age)
}

object EncodersExample extends App {
  val idCard = IdCard("6937745", Person("Viktor Marinov", 25))

  println(idCard.asJson.spaces2)

  println(idCard.asJson.spaces4)
  println(idCard.asJson.noSpaces)
  println(idCard.asJson.spaces2SortKeys)

  val idCard2 = IdCard("37842634", Person("Zdravko", 21))
  println(List(idCard, idCard2).asJson)

  val maybeIdCard = Some(idCard)
  val maybeIdCard2 = None
  println(maybeIdCard.asJson)
  println(maybeIdCard2.asJson)
}

object DecodersExample extends App {
  val json =
    """{
      |  "id" : "6937745",
      |  "person" : {
      |    "name" : "Viktor Marinov",
      |    "age" : 25
      |  }
      |}""".stripMargin

  println(decode[IdCard](json))


  val wrongJson =
    """{
      |  "id" : "6937745",
      |  "person" : {
      |    "name" : "Viktor Marinov"
      |  }
      |}""".stripMargin

  println(decode[IdCard](wrongJson))
}

object ParserExample extends App {
  val json =
    """{
      |  "id" : "6937745",
      |  "person" : {
      |    "name" : 23
      |    "age" : 25
      |  }
      |}""".stripMargin


  val parseResult: Either[ParsingFailure, Json] = parse(json)
  println(parseResult)
}
