package answers

import answers.TailRecursion.fibonacci

import scala.annotation.tailrec

object Recursion extends App {

  def fact(n: Int): Int =
    if (n <= 1) 1
    else n * fact(n - 1)

  def size[A](l: List[A]): Int =
    if (l.isEmpty) 0
    else 1 + size(l.tail)

  def sum(l: List[Int]): Int =
    if (l.isEmpty) 0
    else l.head + sum(l.tail)

  def fibonacci(i: Int): Int = {
    if (i <= 0) 0
    else if (i == 1) 1
    else fibonacci(i - 1) + fibonacci(i - 2)
  }
}

object TailRecursion extends App {

  // We could introduce inner functions if we don't want to pollute the interface

  @tailrec
  def fact(n: Int, acc: Int = 1): Int =
    if (n <= 1) acc
    else fact(n - 1, acc * n)

  @tailrec
  def size[A](l: List[A], acc: Int = 0): Int =
    if (l.isEmpty) acc
    else size(l.tail, acc + 1)

  @tailrec
  def sum(l: List[Int], acc: Int = 0): Int =
    if (l.isEmpty) acc
    else sum(l.tail, l.head + acc)

  @tailrec
  def fibonacci(i: Int, current: Int = 0, next: Int = 1): Int =
    if (i <= 0) current
    else fibonacci(i - 1, current = next, next = current + next)

}

object MoreListFunctions extends App {

  @tailrec
  def drop[A](la: List[A], n: Int): List[A] =
    if (n <= 0 || la.isEmpty) la
    else drop(la.tail, n - 1)

  @tailrec
  def reverse[A](l: List[A], acc: List[A] = Nil): List[A] =
    if (l.isEmpty) acc
    else reverse(l.tail, l.head :: acc)

  def take[A](la: List[A], n: Int): List[A] = {
    @tailrec
    def loop(rest: List[A], k: Int, acc: List[A] = Nil): List[A] = {
      if (k == 0) acc
      else loop(rest.tail, k - 1, rest.head :: acc)
    }

    reverse(loop(la, n))
  }

  @tailrec
  def nthElement[A](la: List[A], n: Int): A =
    if (n == 0) la.head
    else nthElement(la.tail, n - 1)

  def concat(l1: List[Int], l2: List[Int]): List[Int] = {
    @tailrec
    def loop(rest: List[Int], acc: List[Int]): List[Int] = {
      if (rest.isEmpty) acc
      else loop(rest.tail, rest.head :: acc)
    }

    loop(reverse(l1), l2)
  }
}
