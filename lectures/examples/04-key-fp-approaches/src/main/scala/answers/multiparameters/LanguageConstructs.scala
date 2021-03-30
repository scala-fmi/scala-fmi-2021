package answers.multiparameters

import answers.TailRecursion.{fact, fibonacci}
import answers.multiparameters.TypeInference.mapML

import scala.annotation.tailrec
import scala.io.Source
import scala.language.reflectiveCalls

object LanguageConstructs extends App {
  // times construct
  implicit class EnrichedInt(val n: Int) extends AnyVal {
    def times(fn: => Unit): Unit =
      if (n <= 0) ()
      else {
        fn
        (n - 1).times(fn)
      }
  }

  4.times {
    println("Meow")
    println("I am a cat")
  }

  @tailrec
  def times(n: Int)(fn: => Unit): Unit =
    if (n <= 0) ()
    else {
      fn
      times(n - 1)(fn)
    }

  times(4) {
    println("Meow")
    println("I am a cat")
  }

  // Higher-ordered functions as constructs
  mapML(List(4, 5, 6)) { n =>
    val factN = fact(n)
    val fibN = fibonacci(n)

    factN + fibN
  }

  List(20, -40, 30).fold(0) { (a, b) =>
    math.max(a.abs, b.abs)
  }

  // safe resources
  type Closeable = { def close(): Unit }

  def using[A <: Closeable, B](resource: A)(f: A => B): B =
    try f(resource)
    finally resource.close() // requires the reflectiveCalls import. Using java.lang.AutoCloseable might be better here

  def numberOfLines(fileName: String): Int =
    using(Source.fromFile(fileName)) { file =>
      file.getLines().size
    }

  println {
    numberOfLines("build.sbt")
  }
}
