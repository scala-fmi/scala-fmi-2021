package http

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s.CacheDirective.`no-cache`
import org.http4s.dsl.io._
import org.http4s.headers.{`Cache-Control`, `WWW-Authenticate`}
import org.http4s.{Challenge, HttpDate, ResponseCookie}

object Example4_Responses {

  // Simple response builders
  Ok()
  Ok().unsafeRunSync()
  NoContent()

  // Response with body
  Ok("Ok response.")
  Ok("Ok response.", `Cache-Control`(NonEmptyList(`no-cache`(), Nil))).unsafeRunSync()
  Unauthorized(`WWW-Authenticate`(Challenge("scheme", "realm", Map.empty)), "Provide xxx")

  Ok(IO.pure("It can also accept IO")).unsafeRunSync()

  // Add cookie util method
  Ok("Ok response.").map(_.addCookie(ResponseCookie("foo", "bar"))).unsafeRunSync()

  val cookieResp = for {
      resp <- Ok("Ok response.")
      now <- HttpDate.current[IO]
    } yield resp.addCookie(ResponseCookie("foo", "bar", expires = Some(now), httpOnly = true, secure = true))
  cookieResp.unsafeRunSync().cookies
}
