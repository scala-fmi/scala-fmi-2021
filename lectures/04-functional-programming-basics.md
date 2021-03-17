---
title: Основни подходи при ФП
---

# За какво ще говорим

::: incremental

* Рекурсия
  * Опашкова рекурсия, практични примери
* Персистентни структури от данни
* Функциите като първокласни обекти
  * ламбда фунцкии и функционален тип
  * Функции от по-висок ред - `map`, `filter`, `foldLeft` и други
  * Currying - защо и кои са средствата, които Скала ни дава
* Композиция на функции
* Изразяване чрез типове

:::

# Рекурсия

::: incremental

* Функция, която извиква себе си
* ![](images/04-functional-programming-basics/captain-obvious.jpg){ height=200 }
* Използва се за постигане на цикличност. Често срещано във ФП
* Избягва мутиране на състояние
* По-естествен начин за описване на определени алгоритми. Пример - fact
  ```scala
  def fact(n: Int): Int =
    if (n <= 1) 1
    else n * fact(n - 1)
  ```

:::

# Примери

```scala
def sum(l: List[Int]): Int

def size[A](l: List[A]): Int

def fibonacci(i: Int): Int
```

# Unfolding the recursion

::: incremental

* Използвайки substitution model
  
  ```
  fact(5)
  --
  5 * fact(5 - 1) =
  --
  5 * fact(4) =
  --
  5 * (4 * fact(4 - 1)) =
  --
  5 * (4 * fact(3)) =
  --
  5 * (4 * (3 * fact(3 - 1))) =
  --
  5 * (4 * (3 * fact(2))) =
  --
  5 * (4 * (3 * (2 * fact(2 - 1)))) =
  --
  5 * (4 * (3 * (2 * fact(1)))) =
  --
  5 * (4 * (3 * (2 * 1)))
  ```

* ако извикаме функцията с `Int.MaxValue` ще получим `java.lang.StackOverflowError`

:::

# Опашкова рекурсия (tail recursion)

::: incremental

> "A recursive function is tail recursive when recursive call is the last thing executed by the function."

:::

# Примери

::: incremental

```scala
def fact(n: Int, acc: Int = 1): Int =
  if (n <= 1) acc
  else fact(n - 1, acc * n)
```

<div class="fragment">

И какво от това?

</div>

:::

# Как изглежда стека тогава?

```
fact(5, 1)
--
fact(5 - 1, 5 * 1) =
--
fact(4, 5) =
--
fact(4 - 1, 5 * 4) =
--
fact(3, 20) =
--
fact(3 - 1, 20 * 3) =
--
fact(2, 60) =
--
fact(2 - 1, 60 * 2) =
--
fact(1, 120) =
--
120
```

# Ето какво

::: incremental

* Няма нужда да пазим променливи в стека от предните извиквания
* "Tail recursive" функциите могат да бъдат оптимизирани от компилатора
* accumulator подход
* @tailrec

:::

# Още примери

Нека преправим предните примери с опашкова рекурсия

```scala
def sum(l: List[Int]): Int

def size[A](l: List[A]): Int

def fibonacci(i: Int): Int
```

# Обхождане на списък

* drop
* reverse
* take 
* nth element
* concat

# Малко повече за функциите

::: incremental

* Функциите в Scala са първокласни обекти.
* Какво означава това?
  * Могат да се използват навсякъде както бихме използвали "нормални" стойности
  * Няма ограничение къде могат да бъдат дефинирани, т.е. като "нормални" стойности
  * Типа им се описва подобно на "нормалните" стойности
* В Scala има Function literals - анонимни функции (ламбда)

:::

# Локални функции

::: incremental

```scala
def foo(a: Int): Int = {
  def bar(b: Int) = b + 10 // local function

  bar(a)
}
```
```scala
def foo(a: Int): Int = {
  def bar(b: Int) = a + b // can also use scope defined above

  bar(42)
}
```

:::

# Анонимни функции, a.k.a lambda 

Синтаксис
```scala
val lambdaName = (param:Type) => expression
```

<div class="fragment">

```scala
val addOne = (x: Int) => x + 1
val sum = (x: Int, y: Int) => x + y

// или така
val addOne: Int => Int = x => x + 1
val sum: (Int, Int) => Int = (x, y) => x + y

val addOne = { x: Int =>    // с къдрави скоби
  val a = x + 1
  a
}

val addOne = (x: Int) => {  // може и така
  x + 1
}

// Извикват се по стандартния начин
addOne(41) // 42
sum(40, 2) // 42
```

</div>


# Функционален тип

::: incremental

* Функциите са обекти, които също си имат тип.
* Когато дефинираме следната функция
  ```scala
  val lessThan = (a: Int, b: Int) => a < b
  ```
* всъщност се дефинира обект от тип `Function2` с метод `apply`
  ```scala
  val lessThan = new Function2[Int, Int, Boolean] {
    def apply(a: Int, b: Int): Boolean = a < b
  }
  ```
* `Function2` e нормален `trait` - репрезентира функции на два аргумента
* Съществуват подобни за функции на различен брой аргументи - `Function0` ... `Function22`

:::

# apply?

::: incremental

* Метод `apply` е специален. Обектите, които го имат могат да бъдат извиквани като функции

<div class="fragment">

```scala
  class Adder(a: Int) {
    def apply(b: Int) = a + b
  }
  
  val oneAdder = new Adder(1)
  oneAdder(2)
  // res2: Int = 3
  ```

</div>

:::


# "Placeholder" синтаксис

::: incremental

* Може да използваме `_`, който ще бъде попълнен с параметър
  ```scala
  val addOne: Int => Int = _ + 1
  
  val addOne = (_: Int) + 1
  
  val describe: Int => String = _.toString
  ```
* Може да използваме за повече от един параметър
  ```scala
  val sum2: (Int, Int) => Int = _ + _
  
  val sum3: (Int, Int, Int) => Int = _ + _ + _
  ```

:::
  
# Partial application

```scala
val addOne = sum(_, 1)
```

The type of `addOne` is `Int => Int` 

<div class="fragment">

```scala
def wrap(prefix: String, html: String, suffix: String) = prefix + html + suffix

val wrapWithP = wrap("<p>", _: String, "</p>")
val wrapWithDiv = wrap("<div>", _: String, "</div>")

wrapWithDiv(wrapWithP("Hello, world"))
// res3: String = "<div><p>Hello, world</p></div>"
```

Трябва да специфицираме типа на параметъра

</p>


# Difference between `def` and `val` functions

maybe delete

# Eta expansion
  ```scala
  def sum(a: Int, b: Int) = a + b

  val sumFun = sum  // doesn't work
  
  val sumFun = sum _  // works

  val sumFun: (Int, Int) => Int = sum   // also works

  val sumFun2: Function2[Int,Int,Int] = sum   // also works

  sumFun(1, 2)
  ```

# Higher-order functions

::: incremental

* Вече видяхме, че са функциите нормални стойности
* Което означава, че можем да ги подаваме на други функции или да ги връщаме като резултати

<div class="fragment">

Higher-order functions

> Функции, които приемат функции като параметри или връщат функции като резултат

</div>

:::

# Пример

```scala
def formatResult(name: String, n: Int, f: Int => Int) = {
  s"The $name of $n is ${f(n)}" 
}

formatResult("factorial", 3, fact)
//res2: String = "The factorial of 3 is 6"

formatResult("+1 addition", 41, addOne)
//res4: String = "The +1 addition of 41 is 42"

```

# filter

![](images/04-functional-programming-basics/filter.png)

# filter

```scala
def filter[A](la: List[A], p: A => Boolean): List[A]

// метод на trait Seq
trait Seq[A] {
  def filter(pred: A => Boolean): Seq[A]
  ...
}
```

# Примери

```scala
val l = List(1, 2, 3, 4, 5, 6)
val isEven = (x: Int) => x % 2 == 0

l.filter(isEven)
// res0: List[Int] = List(2, 4, 6)

l.filter(x => x > 3)    // uses Type Inference
// res1: List[Int] = List(4, 5, 6)

l.filter(_ < 3)
// res2: List[Int] = List(1, 2)
```

# map

![](images/04-functional-programming-basics/map.png)

# map

```scala
def map[A, B](la: List[A], f: A => B): List[B]

// метод на trait Seq
trait Seq[A] {
  def map[B](f: A => B): Seq[B]
  ...
}
```

# Примери

```scala

List(1, 2, 3).map(_ * 2)
// res0: List[Int] = List(2, 4, 6)

List("foo", "bar", "baz").map(wrapWithDiv)
//res1: List[String] = List("<div>foo</div>", "<div>bar</div>", "<div>baz</div>")

```

# Имплементация

```scala

def filter[A](la: List[A], p: A => Boolean): List[A] = ???

def map[A, B](la: List[A], f: A => B): List[B] = ???

```

# chaining

```scala
List("foo", "bar", "bazzzz")
  .filter(_.length < 5)
  .map(wrapWithP)
  .map(wrapWithDiv)

// res0: List[String] = List("<div><p>foo</div></div>", "<div><p>bar</div></div>")
```

# Синтаксис с блок

```scala
val l = List("foo", "bar", "baz") 
l.map { s =>
  println(s)
  wrapWithDiv(s)
}
// res0: List[String] = List("<div>foo</div>", "<div>bar</div>", "<div>baz</div>")

//maybe delete that - show with partial functions
l.map {
  case "foo" => wrapWithP("foo")
  case s => wrapWithDiv(s)
}
// res1: List[String] = List("<p>foo</div>", "<div>bar</div>", "<div>baz</div>")
```

# Допълнителни ресурси

* [Scala: The Differences Between `val` and `def` When Creating Functions](https://alvinalexander.com/scala/fp-book-diffs-val-def-scala-functions/)
* [Partially-Applied Functions (and Currying) in Scala](https://alvinalexander.com/scala/fp-book/partially-applied-functions-currying-in-scala/)

