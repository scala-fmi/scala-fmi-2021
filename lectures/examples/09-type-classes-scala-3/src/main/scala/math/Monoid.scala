package math

// Compiles only in scala 3

trait Monoid[M] {
  extension (a: M) def |+|(b: M): M
  def identity: M
}

object Monoid {
  def apply[M](using Monoid[M]) = summon[Monoid[M]]

  given Monoid[Int] with {
    extension (a: Int) def |+|(b: Int): Int = a + b
    def identity: Int = 0
  }

  val intMultiplicativeMonoid = new Monoid[Int] {
    extension (a: Int) def |+|(b: Int): Int = a * b
    val identity: Int = 1
  }

  given Monoid[String] with {
    extension (a: String) def |+|(b: String): String = a + b
    val identity: String = ""
  }

  given [A : Monoid]: Monoid[Option[A]] with {
    extension (a: Option[A]) def |+|(b: Option[A]): Option[A] = (a, b) match {
      case (Some(n), Some(m)) => Some(n |+| m)
      case (Some(_), _) => a
      case (_, Some(_)) => b
      case _ => None
    }

    def identity: Option[A] = None
  }

  given [A : Monoid, B : Monoid]: Monoid[(A, B)] with {
    extension (a: (A, B)) def |+|(b: (A, B)): (A, B) = (a, b) match {
      case ((a1, a2), (b1, b2)) => (a1 |+| b1, a2 |+| b2)
    }

    def identity: (A, B) = (Monoid[A].identity, Monoid[B].identity)
  }

  given [K, V : Monoid]: Monoid[Map[K, V]] with {
    extension (a: Map[K, V]) def |+|(b: Map[K, V]): Map[K, V] = {
      val vIdentity = Monoid[V].identity

      (a.keySet ++ b.keySet).foldLeft(identity) { (acc, key) =>
        acc + (key -> (a.getOrElse(key, vIdentity) |+| b.getOrElse(key, vIdentity)))
      }
    }

    def identity: Map[K, V] = Map.empty[K, V]
  }
}
