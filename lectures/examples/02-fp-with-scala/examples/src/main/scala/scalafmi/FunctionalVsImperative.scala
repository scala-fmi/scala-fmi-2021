package scalafmi

object FunctionalVsImperative extends App {
  def fn = {
    // with lazy val any order works
    //
    // with just val we will get compile error if something is not right
    // (it won't succeed silently like in imperative programis)

    lazy val y = b * 40
    lazy val c = 50

    lazy val a = 10
    lazy val z = y * x * x

    lazy val b = 40
    lazy val x = a + c

    z
  }
}
