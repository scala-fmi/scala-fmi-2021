---
title: ООП във функционален език
---

# Предния път

::: incremental

* Типова йеархия. `Any`, `AnyVal`, `AnyRef`, `Unit`, `Null`, `Nothing`
* "Чисти" контролни структури -- if, pattern matching, for
* "Нечисти" (референтно непрозрачни) контролни структури -- while, try/catch, side-effecting for
* Съставни структури -- n-торки, `Range`, `List`, `Set`, `Map`
* Функционално и императивно програмиране -- що са те?
* Модели на изчисление

:::

# Задача

Напишете функция, проверяваща, че скобите в един израз са балансирани

```scala
def balanced(e: List[Char]): Boolean = ???
```

# Обектно-ориентирано програмиране

?

# Кой е това?

![](images/03-oop-in-a-functional-language/alan-kay.jpg){ height=512 }

# Кой е това?

![](images/03-oop-in-a-functional-language/alan-kay.jpg){ height=384 }
![](images/03-oop-in-a-functional-language/alan-kay-raising-hand.png){ height=384 }

::: { .fragment }

Alan Kay<span class="fragment"><br/>предлага термина ООП (c. 1967)</span>

:::

# ООП?

> “I made up the term ‘object-oriented’, and I can tell you I didn’t have C++ in mind.” -- Alan Kay

# [Dr. Alan Kay on the Meaning of<br />“Object-Oriented Programming”](http://userpage.fu-berlin.de/~ram/pub/pub_jf47ht81Ht/doc_kay_oop_en)

> “I thought of objects being like biological cells and/or individual computers on a network, only able to communicate with messages… <span class="fragment">OOP to me means only messaging, local retention and protection and hiding of state-process, and extreme late-binding of all things.” -- Alan Kay</span>

::: { .fragment }

Забележете, че не се споменават класове, наследяване и др.

:::

# В основата на ООП -- съобщенията

![](images/03-oop-in-a-functional-language/messaging.png){ height=256 style="border-radius: 10px" }

::: incremental

* Множество познати ни ООП принципи (като SOLID) се фокусират върху практики за дизайн на **един** клас
* ООП всъщност е:
  - система от обекти,
  - комуникиращи помежду си
* Добър ООП дизайн обхваща цялостната комуникация и участващите обекти
* В познатите езикови ни конструкции съобщения са методите

:::

# Енкапсулация

::: incremental

* getter-и, setter-и, copy конструктори и т.н. НЕ са ООП
* Всъщност са антитеза на ООП
* Обектите не са структури от данни
* Енкапсулацията при обектите се отнася до това, че скриват своето състояние и структурите, които използват, от другите обекти
* Обектите си взаимодействат единствено през ясен протокол (интерфейс/възможни съобщения) -- поведение на обекта
* При ООП липсва споделено състояние

:::

# Енкапсулация - пример

`Range`, `List`, `Set`, `Map` -- всеки има напълно различна имплементация, но общ протокол на комункация

# Комуникация в биологията<br />(паралел)

::: incremental

* Нашето тяло е сложна система от комуникиращи си клетки/органи/...
* Различни среди на комуникация
  - хормони
  - сигнали по нервната система
  - дори вътре в клетката – ядрото изпраща messenger RNA към рибозомите

:::

![](images/03-oop-in-a-functional-language/covid-vaccine.webp){ height=256 .fragment style="border-radius: 20px;" }

# Late Binding

::: incremental

* Конкретното поведение, което ще се изпълни, се разбира едва по време на изпълнение
* Подтиповия полиморфизъм е един аспект на late binding
* В по-голям мащаб: подмяна на части от системата без да се спира цялата система

:::

# [ООП + ФП?](https://www.quora.com/Why-is-functional-programming-seen-as-the-opposite-of-OOP-rather-than-an-addition-to-it/answer/Alan-Kay-11)

::: incremental

* ООП не предполага странични ефекти
* Може да се използва по immutable и функционален подход

:::

::: { .fragment }

> "So: both OOP and functional computation can be completely compatible (and should be!). There is no reason to munge state in objects, and there is no reason to invent “monads” in FP. We just have to realize that “computers are simulators” and figure out what to simulate." -- Alan Kay

:::

::: { .fragment }

> "I will be giving a talk on these ideas in July in Amsterdam (at the 'CurryOn' conference)."

:::

::: { .fragment }

Не дойде 😭

:::

# ООП и дистрибутирани системи

::: incremental

* Алан Кей говори за ООП в контекста на компютри в мрежа
* Дистрибутираните системи са недетерминирани по природа
* Но дори при тях ФП може да помогне много
* Езици като Erlang ги моделират по възможно най-функционален подход
  - обекти (а.к.а. процеси/актьори) със чисто функционално поведение
  - недерменирана комуникация чрез съобщения между тях
* В Scala – библиотеката Akka
* Тема на друг курс

:::

# ООП в Scala

* Типизация
* Класове, обекти, интерфейси и др.
* Uniform Access Principle
* Модулярност чрез ООП конструкции
* Подтипов полиморфизъм и late binding
* Scala и the expression problem?
* Extension методи

# Дефиниране на клас

* Параметри на клас -- конструктор
* Членове
* Модификатори на достъп

::: { .fragment }

Да дефинираме клас `Rational`

:::

# Дефиниране на обект

::: { .fragment }

```scala
object Math {
  val Pi = 3.14159

  def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
  
  def squared(n: Int) = n * n
}

Math.Pi
Math.gcd(27, 12)
```

:::

::: { .fragment }

```scala
val m: Math.type = Math
m.squared(9)
```

:::

# `apply` методи

Всеки обект с `apply` метод може да бъде използван като функция:

::: { .fragment }

```scala
object AddTwo {
  def apply(n: Int): Int = n + 2
}

val theLongAnswer = AddTwo.apply(40) // 42
val theAnswer = AddTwo(40) // 42
```

:::

# `apply` методи

```scala
class Interval(a: Int, b: Int, inclusive: Boolean = true) {
  require(a <= b)
  
  def apply(n: Int) =
    if (inclusive) a <= n && n <= b
    else a < n && n < b
}

val percentageInterval = new Interval(0, 100)
percentageInterval(42) // true
percentageInterval(110) // false
```

# Обекти-другарчета (придружаващи/companion обекти)

::: incremental

* В Scala класовете нямат статични методи
* Вместо това помощни функции могат да бъдат дефинирани в техните придружаващи обекти 🤝
* Обект придружава клас, ако
  - е дефиниран със същото име като класа и
  - се намира в същия файл

:::

# Обекти-другарчета (придружаващи/companion обекти)

```scala
class Rational {
  // ...
}

object Rational {
  val Zero = Rational(0) // използва apply, дефиниран долу
  
  def apply(n: Int, d: Int = 1) = new Rational(n, d)
  
  def sum(rationals: Rational*): Rational =
    if (rationals.isEmpty) Zero
    else rationals.head + sum(rationals.tail)
}

Rational.sum(Rational(1, 2), Rational(5), Rational(3, 5))
```

# Придружаващи обекти

`List(1, 2, 3)` се свежда до `List.apply(1, 2, 3)`,<br />което е функция с променлив брой параметри

# Придружаващи обекти

Имат достъп и до private/protected членовете:

```scala
class Rational private (n: Int, d: Int) {
  private def toDouble = n.toDouble / d
  
  // ...
}

object Rational {
  def apply(n: Int, d: Int = 1) = new Rational(n, d)
  
  def isSmaller(a: Rational, b: Rational) = a.toDouble < b.toDouble
}

Rational.isSmaller(Rational(1, 2), Rational(3, 4)) // true
```

# implicit конверсия

```scala
Rational(2, 3) + 1 // грешка при компилиране, + приема Rational, не Int
```

::: { .fragment }

```scala
implicit def intToRational(n: Int): Rational = Rational(n)

Rational(2, 3) + 1 // Rational(5, 3), работи
```

:::

# implicit конверсия

```scala
implicit def intToRational(n: Int): Rational = Rational(n)

Rational(2, 3) + 1 // Rational(5, 3)
1 + Rational(2, 3)  // Rational(5, 3), също работи
```

::: { .fragment }

Преобразува се до:

```scala
Rational(2, 3) + intToRational(1) // Rational(5, 3)
intToRational(1) + Rational(2, 3)  // Rational(5, 3), също работи
```

:::

::: { .fragment }

Когато компилаторът не открие метод с очакваните име и параметри<br />решава да потърси за възможна имплицитна конверсия към тип,<br />който има този метод

:::

# implicit конверсия -- ред на търсене

1. В текущия scope (чрез текущ или външен блок или чрез import)
2. В продружаващия обект на който и да е от участващите типове

# Още за implicit конверсии

::: incremental

* Добре е да се ограничават
* Изискват `import scala.language.implicitConversions`
* Препоръчително е използването на конверсии с по-конкретни типове пред по-общи
* `Ctrl+Alt+Shift и +` в IntelliJ показва implicit конверсиите
* `Ctrl+Alt+Shift и -` ги скрива

:::

# case класове

```scala
case class Person(name: String, age: Int, address: String)

val vasil = Person("Vasil", 38, "Sofia")
```

::: incremental

* неизменим value клас
* всички изброени параметри автоматично стават `val` полета
* автоматично генериране на:
  - придружаващ обект с `apply`
  - `equals`, `hashCode`, `toString`
    ```scala
      Person("Vasil", 38, "Sofia") == Person("Vasil", 38, "Sofia") // true
    ```
  - `copy` -- позволява инстанциране на нова версия, базирана на съществуващата
    ```scala
       def getOlder(person: Person): Person = person.copy(age = person.age + 1)
    ```
  - още няколко удобства -- за тях по-натам

:::

# Влагане на case класове

```scala
case class Person(name: String, age: Int, address: Address)
case class Address(country: String, city: String, street: String)

val radost = Person("Radost", 24, Address("Bulgaria", "Veliko Tarnovo", "ul. Roza"))
```

# Поведение на case класове

```scala
case class Circle(radius: Double) {
  def area = math.Pi * radius * radius
}
```

```scala
case class Person(name: String, age: Int, address: Address) {
  def sayHiTo(person: Person): String =
    s"Hi ${person.name}! I am $name from ${address.country}"
}
```

# Универсален apply { .scala3 }

В Scala 3 автоматично се генерира придружаващ обект с apply за всеки клас (не само за case класовете):

::: { .fragment }

```scala
class Rational(n: Int, d: Int)

Rational(1, 2) // работи
new Rational(1, 2) // също работи
```

:::

# Абстрактни типове -- trait

```scala
trait Ordered[A] {
  def compare(that: A): Int
  
  def <(that: A): Boolean = compare(that) < 0
  def <=(that: A): Boolean = compare(that) <= 0
  def >(that: A): Boolean = compare(that) > 0
  def >=(that: A): Boolean = compare(that) >= 0
}
```

# Абстрактни типове -- trait

```scala
class Rational(n: Int, d: Int) extends Ordered[Rational]) {
  // ...

  def compare(that: Rational): Int = (this - that).numer
}

Rational(3, 4) < Rational(1, 2) // false
```

# Uniform Access Principal

```scala
trait Humanoid {
  def name: String
  def age: Int
}
```

::: { .fragment }

```scala
class Person(n: String, a: Int) extends Humanoid {
  val name = n
  val age = a
}
```

:::

::: { .fragment }

```scala
class Robot(brand: String, serialNumber: String, a: Int) extends Humanoid {
  def name = s"$brand--$serialNumber"
  val age = a
}
```

:::

::: { .fragment }

UAC -- интерфейсът не се променя от това дали дадено име е имплементирано чрез ичисление (`def`)<br />или чрез съхранена стойност (`val`)

:::

# Uniform Access Principal и case класове

```scala
trait Humanoid {
  def name: String
  def age: Int
}

case class Person(name: String, age: Int) extends Humanoid
case class Robot(brand: String, serialNumber: String, age: Int) extends Humanoid {
  def name = s"$brand--$serialNumber"
}
```

# Множествено наследяване

```scala
trait A {
  val hello = "Hello"
}
trait B extends A
trait C extends A

class X extends B with C

new X().hello // Hello, diamond структура не създава проблем
```

# Множествено наследяване

```scala
trait A {
  def hello(to: String): String = s"Hello to $to from A"
}
trait B extends A {
  override def hello(to: String): String = s"Hello to $to from B"
}
trait C extends A {
  override def hello(to: String): String = s"Hello to $to from C"
}

class X extends B with C

new X().hello("FMI") // Hello to FMI from C
```

::: incremental

* trait-овете по-вдясно override-ват имплементацията от trait-овете вляво
* Има възможност да работи като декорация, ако ви е любопитно попитайте ни за пример в Slack

:::

# Подтипов полиморфизъм

```scala
trait Shape {
  def name: String
  def area: Double
}

case class Circle(r: Double) extends Shape {
  def name = "circle"
  def area: Double = math.Pi * r * r
}

case class Rectangle(a: Double, b: Double) extends Shape {
  def name = "rectangle"
  def area: Double = a * b
}

val shape: Shape = Circle(2)
shape.area

// типът на фигурата се определя по време на изпълнение
val randomShape: Shape = getRandomShape()
randomShape.area // програмата знае коя имплементация да използва
```

# Рефиниране/имплементиране на типове при инстанциране

```scala
val unitSquare = new Shape {
  val name = "square"
  def area = 1
}
```

# trait параметри { .scala3 }

```scala
trait Greeting(name: String):
   def hello = s"Hello, I am $name"

case class Person(name: String) extends Greeting(name)

Person("Dimitar").hello // Hello, I am Dimitar
```

# import клаузи

::: { .fragment }

```scala
import scala.util.Try // само типа Try

Try(10)
```

:::

::: { .fragment }

```scala
import scala.util._ // всичко от util пакета

Try(10)
Success(10)
```

:::

::: { .fragment }

```scala
import math.Math.{ gcd, Pi } // няколко неща от обекта Math

gcd(42, 18) * Pi
```

:::

# import клаузи

```scala
import math.Math._ // всичко от oбекта Math

squared(11)
gcd(42, 10)
```

::: { .fragment }

```scala
import scala.collection.immutable.Set
import scala.collection.mutable // импорт на част от пътя

Set(1, 2, 3)
mutable.Set(4, 5, 6)
```

:::

::: { .fragment }

```scala
import scala.collection.immutable.Set
import scala.collection.mutable.{ Set => MutableSet } // преименуване

Set(1, 2, 3)
MutableSet(4, 5, 6)
```

:::

# import клаузи

::: incremental

* Могат да са във всеки scope, не е нужно да са в началото на файла:
  
  ```scala
  class Rational(n: Int, d: Int) {
    import Math.gcd
    
    gcd(n.abs, d.abs)
    // ...
  }
  ```
* Автоматично във всеки файл се включват следните import-и:
  ```scala
  import java.lang._
  import scala._
  import scala.Predef._
  ```

:::

# export клаузи { .scala3 }

Позволяват делегация:

```scala
object IntUtils {
  def twice(n: Int): Int = 2 * n
  def squared(n: Int): Int = n * n
}

object DoubleUtils {
  def twice(n: Double): Double = 2 * n
  def squared(n: Double): Double = n * n
}

object MathUtils {
  export IntUtils._
  export DoubleUtils._
}

MathUtils.twice(2) // 4
MathUtils.twice(2.0) // 4.0
```

::: incremental 

* export-натите имена стават членове на обекта
* синтактично е със същия формат като `import`

:::

# export клаузи { .scala3 }

```scala
class Scanner {
  def scan(bitMap: Image): Page = ???
  def isOn: Boolean = ???
}

class Printer {
  def print(page: Page): Image = ???
  def isOn: Boolean = ???
}

class Copier {
  private val scanner = new Scanner
  private val printer = new Printer
  
  export scanner.scan
  export printer.print
  
  def isOn = scanner.isOn && printer.isOn
}

val copier = new Copier
val image = ???
val copiedImage = copier.print(copier.scan(bitMap))

image == copiedImage // true, hopefully :D
```

# AnyVal класове

```scala
case class PersonId(id: String) extends AnyVal
case class LocationId(id: String) extends AnyVal

def createAddressRegistration(person: PersonId, location: LocationId) = ???
```

::: { .fragment }

```scala
val stoyan = PersonId("100")
val ruse = LocationId("5")
createAddressRegistration(stoyan, ruse) // успех
createAddressRegistration(ruse, stoyan) // грешка, не е възможно да ги объркаме
```

:::

::: incremental

* не създават допълнителен обект, вместо това се репрезентират от типа, който wrap-ват
* носят повече type safety в някои ситуации

:::

# AnyVal класове

```scala
case class Meter(amount: Double) extends AnyVal {
  def +(m: Meter): Meter = Meter(amount + m.amount)
  def *(coefficient: Double): Meter = Meter(coefficient * amount)
  
  override def toString = s"$amount meters"
}
```

::: { .fragment }

```scala
case class Circle(radius: Meter) {
  def circumference: Meter = radius * 2 * math.Pi
}

Circle(Meter(2)).circumference.toString // 12.566370614359172 meters
```

:::

::: incremental

* техните методи се извикват статично

:::

# Типизиране -- съвместимост на типове

```scala
val a: A = new B

// кога тип B е съвместим с тип A?
```

::: incremental

* Номинално -- типове се проверяват за съвместимост по тяхното име (и по явна релация с други имена)
  - Аз съм бухал, защото са ми казали, че съм бухал
  - Аз като бухал съм птица, защото всички бухали са птици
  - "B наследява A"
* Структурно -- съвместимост на типове се определя по структурата на обекта (по неговото поведение)
  - Аз съм бухал, защото гукам като бухал и защото мога да летя
  - Аз като бухал съм птица, защото мога да летя
  - "B има същите методи (т.е. същата структура) като A"
  
:::

# Структурно типизиране в Scala

```scala
case class Eagle(name: String) {
  def flyThrough(location: String): String =
    s"Hi, I am old $name and I am looking for food at $location."
}

case class Owl(age: Int) {
  def flyThrough(location: String): String =
    s"Hi, I am a $age years old owl and I am flying through $location. Hoot, hoot!"
}
```

::: { .fragment }

```scala
def checkLocations(locations: List[String],
                   bird: { def flyThrough(location: String): String }): List[String] = 
  for {
    location <- locations
  } yield bird.flyThrough(location)

checkLocations(List("Sofia", "Varna"), Owl(7))
```

:::

# Структурно типизиране в Scala

```scala
case class Eagle(name: String) {
  def flyThrough(location: String): String =
    s"Hi, I am old $name and I am looking for food at $location."
}

case class Owl(age: Int) {
  def flyThrough(location: String): String =
    s"Hi, I am a $age years old owl and I am flying through $location. Hoot, hoot!"
}
```

```scala
type Bird = {
  def flyThrough(location: String): String
}

def checkLocations(locations: List[String], bird: Bird): List[String] = for {
  location <- locations
} yield bird.flyThrough(location)

checkLocations(List("Sofia", "Varna"), Eagle("Henry"))
```

# Типова алгебра { .scala3 }

::: { .fragment }

Scala 3 добавя обединение (`|`) и сечение (`&`) на типове

:::

# Сечение на типове { .scala3 }

```scala
trait LovingAnimal {
  def name: String
  def hug = s"A hug from $name"
}

case class Owl(name: String, age: Int) {
  def flyThrough(location: String): String = s"Hi, I am a $age years old owl. Hoot, hoot!"
}

val lovelyOwl: Owl & LovingAnimal = new Owl("Oliver", 7) with LovingAnimal
lovelyOwl.hug // A hug from Oliver
```

# Обединение на типове

```scala
def toInteger(value: String | Int | Double): Int = value match {
  case n: Int => n
  case s: String => s.toInt
  case d: Double => d.toInt
}

toInteger("10") // 10
toInteger(10) // 10
toInteger(10.0) // 10
toInteger(List(10)) // не се компилира
```

# The Expression Problem

::: {.fragment}

> The goal is to define a datatype by cases, where one can add new cases to the datatype and new functions over the datatype, without recompiling existing code, and while retaining static type safety (e.g., no casts).

:::

# The Expression Problem (алтернативно)

::: incremental

* Добавяне на нов тип без промяна на съществуващия код
* Добавяне на нова операция без промяна на съществуващия код

:::

# ООП подход

```scala
trait Shape {
  def area: Double
}

case class Circle(r: Double) extends Shape {
  def area: Double = math.Pi * r * r
}

case class Rectangle(a: Double, b: Double) extends Shape {
  def area: Double = a * b
}
```

# ФП подход

```scala
trait Shape
case class Circle(r: Double) extends Shape
case class Rectangle(a: Double, b: Double) extends Shape

def area(s: Shape): Double = s match {
  case Circle(r) => math.Pi * r * r
  case Rectangle(a, b) => a * b
}
```

::: { .fragment }

`case` класовете могат да бъдат използвани в pattern matching

:::

# Добавяне на операция във ФП -- лесно

```scala
def circumference(s: Shape): Double = s match {
  case Circle(r) => 2 * math.Pi * r
  case Rectangle(a, b) => 2 * (a + b)
}
```

# Добавяне на операция в ООП -- трудно, промяна на всички класове

```scala
trait Shape {
  def area: Double
  def circumference: Double
}

case class Circle(r: Double) extends Shape {
  def area: Double = math.Pi * r * r
  def circumference = 2 * math.Pi * r
}

case class Rectangle(a: Double, b: Double) extends Shape {
  def area: Double = a * b
  def circumference = 2 * (a + b)
}
```

# Добавяне на тип в ООП -- лесно

```scala
case class Square(a: Double) extends Shape {
  def area: Double = a * a
  def circumference: Double = 4 * a
}
```

# Добавяне на тип във ФП -- трудно

```scala
case class Square(a: Double) extends Shape

def area(s: Shape): Double = s match {
  case Circle(r) => math.Pi * r * r
  case Rectangle(a, b) => a * b
  case Square(a) => a * a
}

def circumference(s: Shape): Double = s match {
  case Circle(r) => 2 * math.Pi * r
  case Rectangle(a, b) => 2 * (a + b)
  case Square(a) => 4 * a
}
```

# The Expression Problem

::: incremental

* Всеки език е добре да предоставя изразни средства и за двата проблема
* ООП подходът е подходящ за типове с предварително неизвестен брой случаи и малко основни операции
* Функционалният подход е подходящ за типове с предварително фиксирани случаи

:::

# Extension Methods

::: incremental

* Добавяне на методи към съществуващи типове
* Само в текущия scope

:::

# Extension Methods { .scala3 }

```scala
extension (n: Int) {
  def squared = n * n
  def **(exp: Double) = math.pow(n, exp)
}

3.squared // 9
2 ** 3 // 8.0
```

::: { .fragment }

import-ват се по името на метода:

```scala
// file IntExtensions.scala
package scalafmi.intextensions

extension (n: Int) {
  def squared = n * n
  def **(exp: Double) = math.pow(n, exp)
}

// file Demo.scala
import scalafmi.intextensions.{ squared, twice }

3.squared // 9
2 ** 3 // 8.0
```

:::

# Extension Methods { .scala3 }

```scala
extension (xs: List[Double]) {
  def avg = xs.sum / xs.size
}

List(1.0, 2.0, 3.0).avg // 2.0
List("a", "b", "c").avg // value avg is not a member of List[String]
```

::: { .fragment }

```scala
extension [A](xs: List[A]) {
  def second = xs.tail.head
}

List(1.0, 2.0, 3.0).second // 2.0
List("a", "b", "c").second // b
```
:::

# Extension Methods в Scala 2

::: incremental

* Scala 2 също позволява добавяне на методи
* Използва се механизма за implicit конверсия

:::

# Extension Methods в Scala 2

```scala
class EnrichedInt(val n: Int) extends AnyVal {
  def squared = n * n
  def **(exp: Double) = math.pow(n, exp)
}
implicit intToEnrichedInt(n: Int) = new EnrichedInt(n)

3.squared // 9
2 ** 3 // 8.0
```

# Extension Methods в Scala 2

```scala
implicit class EnrichedInt(val n: Int) extends AnyVal {
  def squared = n * n
  def **(exp: Double) = math.pow(n, exp)
}

3.squared // 9
2 ** 3 // 8.0
```

# Примери от стандартната библиотека

```scala
1 -> "One" // (1, "One"), -> се добавя към всички типове

// extension methods се използва за добавяне на методите за колекции върху String
"abcdef".take(2) // ab

import scala.concurrent.duration.DurationInt
5.seconds // scala.concurrent.duration.FiniteDuration = 5 seconds
```

# DSL за тестове

```scala
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ExampleSpec extends AnyFlatSpec with Matchers {
  "+" should "sum two numbers" in {
    2 + 3 should be (5)
  }
}
```

# Изброими типове чрез DSL (Scala 2)

```scala
object WeekDay extends Enumeration {
  type WeekDay = Value
  
  val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
}

import WeekDay._
def isWorkingDay(day: WeekDay) = d != Sat && d != Sun

isWorkingDay(Wed) // true, :(
```

# ООП дизайн?

::: { .fragment }

* книги за домейн дизайн
  - [Functional and Reactive Domain Modeling](https://www.manning.com/books/functional-and-reactive-domain-modeling)
  - [Domain-Driven Design Distilled](https://www.informit.com/store/domain-driven-design-distilled-9780134434421)
  - [Domain-Driven Design](https://www.informit.com/store/domain-driven-design-tackling-complexity-in-the-heart-9780321125217)

:::

# Таблица на типовите елементи в Scala

# Въпроси :)?