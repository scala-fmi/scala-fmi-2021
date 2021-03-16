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
