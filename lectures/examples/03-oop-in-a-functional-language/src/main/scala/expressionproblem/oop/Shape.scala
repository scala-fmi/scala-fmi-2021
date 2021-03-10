package expressionproblem.oop

trait Shape {
  def area: Double
}

case class Circle(r: Double) extends Shape {
  def area: Double = math.Pi * r * r
}

case class Rectangle(a: Double, b: Double) extends Shape {
  def area: Double = a * b
}
