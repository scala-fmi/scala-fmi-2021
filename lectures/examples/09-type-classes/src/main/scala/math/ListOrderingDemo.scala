package math

object ListOrderingDemo extends App {
  implicit def listOrdering[A: Ordering]: Ordering[List[A]] = new Ordering[List[A]] {
    def compare(x: List[A], y: List[A]): Int = {
      val aOrdering = Ordering[A]
      import aOrdering.mkOrderingOps

      @tailrec
      def loop(x: List[A], y: List[A]): Int = {
        if (x.isEmpty && y.isEmpty) 0
        else if (x.isEmpty) -1
        else if (y.isEmpty) 1
        else if (x.head < y.head) -1
        else if (y.head < x.head) 1
        else loop(x.tail, y.tail)
      }

      loop(x, y)
    }
  }

  val sortedList = List(
    List(1, 2, 3),
    List(3, 4),
    List.empty[Int],
    List(1, 1)
  ).sorted

  println(sortedList)
}
