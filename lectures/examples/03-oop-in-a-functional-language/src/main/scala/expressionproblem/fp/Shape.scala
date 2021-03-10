package expressionproblem.fp

trait Shape
case class Circle(r: Double) extends Shape
case class Rectangle(a: Double, b: Double) extends Shape

object Shape {
  def area(s: Shape): Double = s match {
    case Circle(r) => math.Pi * r * r
    case Rectangle(a, b) => a * b
  }
}