package math

object OrderingTypeClassDemo extends App {
  def quickSort(xs: List[Int]): List[Int] = {
    xs match {
      case Nil => Nil
      case x :: rest =>
        val (before, after) = rest partition { _ < x }
        quickSort(before) ++ (x :: quickSort(after))
    }
  }


  quickSort(List(5, 1, 2, 3))
}
