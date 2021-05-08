package json

case class Person(name: String, email: String, age: Int)

object Person {
  given JsonSerializable[Person] with {
    extension (person: Person) def toJson: JsonValue = JsonObject(Map(
      "name" -> JsonString(person.name),
      "email" -> JsonString(person.email),
      "age" -> JsonNumber(person.age)
    ))
  }
}
