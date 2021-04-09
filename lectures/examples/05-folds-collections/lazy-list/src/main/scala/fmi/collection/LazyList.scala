package fmi.collection

import scala.annotation.tailrec

import Function.tupled

sealed trait LazyList[+A] {
  def head: A
  def tail: LazyList[A]

  def isEmpty: Boolean

  def toList: List[A] = {
    @tailrec
    def loop(xs: LazyList[A], acc: List[A]): List[A] = {
      if (xs.isEmpty) acc.reverse
      else loop(xs.tail, xs.head :: acc)
    }

    loop(this, Nil)
  }
}

class LazyCons[+A](h: => A, t: => LazyList[A]) extends LazyList[A] {
  lazy val head: A = h
  lazy val tail: LazyList[A] = t

  def isEmpty: Boolean = false
}

object LazyNil extends LazyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException
  def tail: LazyList[Nothing] = throw new UnsupportedOperationException

  def isEmpty: Boolean = true
}

object LazyCons {
  def apply[A](h: => A, t: => LazyList[A]): LazyCons[A] = new LazyCons[A](h, t)
}

object LazyList extends App {
  def from(start: Int, step: Int = 1): LazyList[Int] = LazyCons(start, from(start + step, step))
  val naturalNumbers: LazyList[Int] = from(0)

  // Can you make these work?
  val fibs: LazyList[Long] = LazyCons(0, LazyCons(1, (fibs zip fibs.tail).map(tupled(_ + _))))
  fibs.take(10).toList
}