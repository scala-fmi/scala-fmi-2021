package streams

import cats.effect.IO
import cats.effect.unsafe.implicits.global

object Fs201BasicExample extends App {
  import fs2.Stream

  val s1 = Stream.empty
  val s2 = Stream.emit(1)
  val s3 = Stream(1, 2, 3)
  val s4 = Stream.emits(List(1, 2, 3))

  val s5 = s3 ++ s4

  println(s5.toList)

  def repeat[F[_], A](stream: Stream[F, A]): Stream[F, A] = {
    stream ++ repeat(stream)
  }

  println(repeat(s3).take(10).toList)

  val effectfulStream = Stream.eval(IO.println("Hellou!!!"))
  println(effectfulStream.compile.toList.unsafeRunSync())

  val effectfulStream2 = Stream.evalSeq(IO(List(1, 2, 3)))
  println(effectfulStream2.compile.fold(0)(_ + _).unsafeRunSync())
}
