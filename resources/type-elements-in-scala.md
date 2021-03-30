# Таблица на елементите, съставящи типовата система на Scala

<table>
  <thead>
    <tr>
      <th>Елемент</th>
      <th>Пример</th>
      <th>Бележки</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th colspan="4" align="left"><i>Типове</i></th>
    </tr>
    <tr>
<th>

Номинален тип

`Int`<br />
`String`<br />
`Person`

</th>
<td>

```scala
val age: Int = 42
val person: Person = Person("John", age)
```

</td>
<td>

[Номинално типизиране](https://scala-fmi.github.io/scala-fmi-2021/lectures/03-oop-in-a-functional-language.html#/%D1%82%D0%B8%D0%BF%D0%B8%D0%B7%D0%B8%D1%80%D0%B0%D0%BD%D0%B5-%D1%81%D1%8A%D0%B2%D0%BC%D0%B5%D1%81%D1%82%D0%B8%D0%BC%D0%BE%D1%81%D1%82-%D0%BD%D0%B0-%D1%82%D0%B8%D0%BF%D0%BE%D0%B2%D0%B5/7)

</td>
    </tr>
    <tr>
<th>

Структурен тип

<div align="left">

```scala
{
  def name: String
  def multiply(n: Int): Int
}
```

</div>

</th>
<td>

```scala
type Person = {
  def name: String
  def sayHiTo(s: String): String
}

case class Robot(name: String) {
  def sayHiTo(s: String) =
    s"Hello ${s}, I am $name"
}

def selfGreet(p: Person) =
  p.sayHiTo(p.name)

selfGreet(Robot("R2D2"))
```

</td>
<td>

[Структурно типизиране](https://scala-fmi.github.io/scala-fmi-2021/lectures/03-oop-in-a-functional-language.html#/%D1%82%D0%B8%D0%BF%D0%B8%D0%B7%D0%B8%D1%80%D0%B0%D0%BD%D0%B5-%D1%81%D1%8A%D0%B2%D0%BC%D0%B5%D1%81%D1%82%D0%B8%D0%BC%D0%BE%D1%81%D1%82-%D0%BD%D0%B0-%D1%82%D0%B8%D0%BF%D0%BE%D0%B2%D0%B5/7)

</td>
    </tr>
    <tr>
<th>

Параметризиран тип

`List[T]`<br />
`Map[K, V]`

</th>
<td>

`List[Int]`,
`Map[Int, String]`

</td>
<td>

`List` се нарича конструктор на тип.<br />
`List[Int]` е конкретен тип

</td>
    </tr>
    </tr>
    <tr>
<th>

Обединение на типове

`A | B`

</th>
<td>

```scala
def registerUser(rf: RegistrationForm):
  RegistrationFormError | User = ???
```

</td>
<td>

`A | B` притежа интерфейса или на `A` или на `B`

Множеството от обекти в `A | B` съвпада с обединението на множествата от обекти в `A` и `B` 

</td>
    </tr>
    </tr>
    <tr>
<th>

Сечение на типове

`A & B`

</th>
<td>

```scala
trait LovingAnimal {
  def name: String
  def hug = s"A hug from $name"
}

case class Owl(name: String, age: Int) {
  def flyThrough(location: String): String =
    s"Hi, I am a $age years old owl. Hoot!"
}

val lovelyOwl: Owl & LovingAnimal = 
  new Owl("Oliver", 7) with LovingAnimal
lovelyOwl.hug // A hug from Oliver
```

</td>
<td>

`A & B` притежава интерфейса и на `A` и на `B`

Обектите в `A & B` са тези, които принадлежат и на `A` и на `B`

</td>
    </tr>
  </tbody>

  <tbody>
    <tr>
      <th colspan="4" align="left"><i>Type bounds (ограничения върху типови параметри)</i></th>
    </tr>
    <tr>
<th>
Горна граница

`A <: Type`
</th>
<td>

```scala
def using[A <: AutoCloseable, B](resource: A)
                                (f: A => B): B =
  try f(resource)
  finally resource.close()
```

</td>
<td>

Параметърът `A` трябва да е съвместип с типа `Type`, т.е.
* ако `Type` е номинален, то `A` да наследява `Type`
* ако `Type` е структурен, то `A` да има структурата на `Type`

</td>
    </tr>
  </tbody>

  <tbody>
    <tr>
      <th colspan="4" align="left"><i>Полиморфизъм</i></th>
    </tr>
    <tr>
<th>Overloading</th>
<td>

```scala
def twice(n: Int) = n * 2
def twice(d: Double) = d * 2
def twice(str: String) = str * 2

twice(10) // 20
twice(10.0) // 20.0
twice("10") // "1010"
```

</td>
<td>
Дефинират се функции с еднакво име, но различни типове
</td>
    </tr>
    <tr>
<th>Полиморфизъм чрез типови параметри (generics)</th>
<td>

```scala
def repeat[A](value: A, times: Int): List[A]

repeat("Hello", 3)
```

</td>
<td>

Функцията работи със всеки един тип за `A`

</td>
    </tr>
    <tr>
<th>Подтипов полиморфизъм</th>
<td>

```scala
trait Shape {
  def area: Double
}

case class Circle(r: Double) extends Shape {
  def area: Double = math.Pi * r * r
}

case class Rectangle(a: Double, b: Double)
  extends Shape {
  def area: Double = a * b
}

val shape: Shape = Circle(2)
shape.area
```

</td>
<td>Имплементацията се определя по време на изпълнение</td>
    </tr>
    <tr>
<th>Структурен полиморфизъм</th>
<td>

```scala
case class Eagle(name: String) {
  def flyThrough(location: String): String =
    s"""Hi, I am $name and
       I am looking for food at $location."""
}

case class Owl(age: Int) {
  def flyThrough(location: String): String =
    s"""Hi, I am a $age years old owl
       flying through $location"""
}

type Bird = {
  def flyThrough(location: String): String
}

def checkLocations(locations: List[String], 
                   bird: Bird): List[String] = 
  for {
    location <- locations
  } yield bird.flyThrough(location)

checkLocations(
  List("Sofia", "Varna"), 
  Eagle("Henry")
)
```

</td>
<td>

Може да бъде използван всеки тип със структурата на `Bird`.

Duck Typing, който се проверява по време на компилация

</td>
    </tr>
  </tbody>
</table>
