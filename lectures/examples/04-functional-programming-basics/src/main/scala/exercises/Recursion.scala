package exercises

import answers.TailRecursion.fibonacci

import scala.annotation.tailrec

object Recursion extends App {

  def fact(n: Int): Int =
    if (n <= 1) 1
    else n * fact(n - 1)

  def size[A](l: List[A]): Int = ???

  def sum(l: List[Int]): Int = ???

  def fibonacci(i: Int): Int = ???
}

object TailRecursion extends App {

  // We could introduce inner functions if we don't want to pollute the interface
  // But let's use default parameters

  def fact(n: Int, acc: Int = 1): Int = ???

  def size[A](l: List[A]): Int = ???

  def sum(l: List[Int]): Int = ???

  def fibonacci(i: Int): Int = ???

}

object MoreListFunctions extends App {

  def drop[A](la: List[A], n: Int): List[A] = ???

  def reverse[A](l: List[A]): List[A] = ???

  def take[A](la: List[A], n: Int): List[A] = ???

  def nthElement[A](la: List[A], n: Int): A = ???

  def concat(l1: List[Int], l2: List[Int]): List[Int] = ???
}
