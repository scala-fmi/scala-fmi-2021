package exercises

object HigherOrderFunctions {

  def filter[A](la: List[A], p: A => Boolean): List[A] = ???

  def map[A, B](la: List[A], f: A => B): List[B] = ???

  def reduce[A](la: List[A], f: (A, A) => A): A = ???

}
