---
title: Основни подходи при ФП
---

# За какво ще говорим по тази тема

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
* По естествен начин за описване на определени алгоритми. Пример - fact
  ```scala
  def fact(n: Int): Int =
    if (n <= 1) 1
    else n * fact(n - 1)
  ```

// * Обикновено се имплементира от две части - дъно и рекурсивно извикване с промяна в параметрите

:::

# Примери

```scala
def sum(l: List[Int]): Int

def size[A](l: List[A]): Int

def fibonacci(i: Int): Int
```

# Unfolding the recursion

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

# Опашкова рекурсия (tail recursion)

::: incremental

> "A recursive function is tail recursive when recursive call is the last thing executed by the function."

* И какво от това?

:::

# Примери

::: incremental

```scala
def fact(n: Int, acc: Int = 1): Int =
  if (n <= 1) acc
  else fact(n - 1, acc * n)
```

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
* accumulator подход - // паралел с императивните итерации - променливите всъщност стават параметри на функцията
* @tailrec

:::

# Още примери

* Нека преправим предните примери с опашкова рекурсия
```scala
def sum(l: List[Int]): Int

def size[A](l: List[A]): Int

def fibonacci(i: Int): Int
```

:::

# Обхождане на списък

::: incremental

* drop
* reverse
* take 
* nth element 
* size 
* concat

:::



# Малко повече за функциите

::: incremental

* Функциите в Scala са първокласни обекти.
* Какво означава това?
  * Могат да се използват навсякъде както бихме използвали "нормални" стойности
  * Няма ограничение къде могат да бъдат дефинирани (с едно изключение за Scala 2)
  * Типа им се описва подобно на "нормалните" стойности
* В Scala има Function literals - анонимни функции (ламбда)

:::

# Примери

::: incremental

* ```scala
  def sum(a: Int, b: Int) = a + b
  ```
* ```scala
  def foo(a: Int): Int = {
    def bar(b: Int) = a + b // can use scope defined above

    bar(42)
  }
  ```
* ```scala
  val sumFun: (Int, Int) => Int = sum
  ``` 

:::


# Анонимни функции, a.k.a lambda 

::: incremental

* Синтаксис
  ```scala
  val lambda = (param:Type) => expression
  ```
* Примери
  ```scala
  val addOne = (x: Int) => x + 1
  val sum = (x: Int, y: Int) => x + y
  ```
  ```scala
  val addOne: Int => Int = x => x + 1
  ```
* С къдрави скоби
  ```scala
  val addOne = { x: Int =>
    val a = x + 1
    a
  }
  ```
* Може и така
  ```scala
  val addOne = (x: Int) => {
    x + 1
  }
  ```
* Извикват се по стандартния начин
  ```scala
  addOne(41) // 42
  ```

:::


# Функционален тип

::: incremental

* Функциите са обекти, които също си имат тип.
* Когато дефинираме следната функция
  ```scala
  val lessThan = (a: Int, b: Int) => a < b
  ```
* всъщност се дефинира обект с метод `apply`
  ```scala
  val lessThan = new Function2[Int, Int, Boolean] {
    def apply(a: Int, b: Int): Boolean = a < b
  }
  ```

# apply?

* Метод `apply` е специален. Обектите, които го имат могат да бъдат извиквани като функции
* `Function2` e нормален `trait` - репрезентира функции на два аргумента
* Съществуват подобни за функции на различен брой аргументи - `Function0` ... `Function22`
* ```scala
  class Adder(a: Int) {
    def apply(b: Int) = a + b
  }
  
  val oneAdder = new Adder(1)
  oneAdder(2) // res: 3
  ```

:::


# "Placeholder" синтаксис

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
  
# Partial application

```scala
val addOne = sum(_, 1)
```

The type of `addOne` is `Int => Int` 

```scala
def wrap(prefix: String, html: String, suffix: String) = prefix + html + suffix

val wrapWithP = wrap("<p>", _: String, "</div>")
val wrapWithDiv = wrap("<div>", _: String, "</div>")

wrapWithDiv(wrapWithP("Hello, world"))
//res0: String = "<div><p>Hello, world</div></div>"
```

Трябва да специфицираме параметъра


# Difference between `def` and `val` functions


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

* Функции, които приемат функции като параметри или връщат функции като резултат

# filter

```scala
def filter[A](la: List[A], p: A => Boolean): List[A]

// метод на trait Seq
trait Seq[A] {
  def filter(pred: A => Boolean): Seq[A]
  ...
}
```

![](images/04-functional-programming-basics/filter.png)

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

```scala
def map[A, B](la: List[A], f: A => B): List[B]

// метод на trait Seq
trait Seq[A] {
  def map[B](f: A => B): Seq[B]
  ...
}
```

![](images/04-functional-programming-basics/map.png)

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

l.map {
  case "foo" => wrapWithP("foo")
  case s => wrapWithDiv(s)
}
// res1: List[String] = List("<p>foo</div>", "<div>bar</div>", "<div>baz</div>")
```

# Допълнителни ресурси

* [Scala: The Differences Between `val` and `def` When Creating Functions](https://alvinalexander.com/scala/fp-book-diffs-val-def-scala-functions/)
* [Partially-Applied Functions (and Currying) in Scala](https://alvinalexander.com/scala/fp-book/partially-applied-functions-currying-in-scala/)

