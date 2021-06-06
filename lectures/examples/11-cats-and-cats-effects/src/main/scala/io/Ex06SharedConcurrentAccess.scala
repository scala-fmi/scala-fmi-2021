package io

import cats.effect._
import cats.effect.implicits._
import cats.effect.std.Queue
import cats.implicits._

class Channel[A](ref: Ref[IO, Int], q: Queue[IO, A]) {
  def take: IO[A] = q.take <* ref.update(_ - 1)

  def put(a: A): IO[Unit] = ref.update(_ + 1) >> q.offer(a)

  def append(as: Seq[A]): IO[Unit] =
    ref.update(_ + as.length) >>
      as.traverse(q.offer).void

  def length: IO[Int] = ref.get
}

object Channel {
  def apply[A](): IO[Channel[A]] = for {
    ref <- IO.ref(0)
    q <- Queue.unbounded[IO, A]
  } yield new Channel(ref, q)
}

object Ex06SharedConcurrentAccess {
  // See tests in ChannelTestSuite
}