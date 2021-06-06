package modularity.c

import modularity.a.A3
import modularity.b.{B2, BModule}

trait CModule {
  def a3: A3
  def b2: B2

  lazy val c = new C(a3, b2)
}
