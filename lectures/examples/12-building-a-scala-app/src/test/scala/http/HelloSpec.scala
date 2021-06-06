package http

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import http.Example1_HttpRoutes.httpApp
import org.http4s._
import org.http4s.implicits._
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class HelloSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  "hello route" should "return status code 200" in {
    val res = httpApp(get(uri"/hello/world"))
    res.map(_.status).asserting(_ shouldBe Status.Ok)
  }

  it should "return hello world message" in {
    val res = httpApp(get(uri"/hello/world"))
    res.flatMap(_.as[String]).asserting(_ shouldBe "Hello, world.")
  }

  "hola route" should "return ¡Hola, Mundo! message" in {
    val res = httpApp(get(uri"/hola/Mundo"))
    res.flatMap(_.as[String]).asserting(_ shouldBe "¡Hola, Mundo!")
  }

  private def get(uri: Uri): Request[IO] = Request[IO](Method.GET, uri)
}
