---
title: Изграждане на Scala приложение
---

# JSON библиотеки

* [circe](https://circe.github.io/circe/) - базира се на cats
* [play-json](https://github.com/playframework/play-json) - първоначално част от Play Framework, извадена в последствие. Използва jackson.
* [json4s](https://github.com/json4s/json4s) - "This project aims to provide a single AST to be used by other scala json libraries"

# [Circe](https://circe.github.io/circe/)

A JSON library for Scala powered by Cats

# JSON types

![](images/12-http4s-and-circe/circe-json-data-types.jpeg)

# Circe workflow

Encoding<br/>
`Data Model -> Encoder -> Json -> String`

<br/>

Decoding<br/>
`String -> Parser -> Json -> HCursor -> Decoder -> Data Model`

# Encoders

```scala
trait Encoder[A] {
  def apply(a: A): Json
}

implicit class EncoderOps[A](val wrappedEncodeable: A) {
  def asJson(implicit encoder: Encoder[A]): Json = encoder(wrappedEncodeable)
  def asJsonObject(implicit encoder: ObjectEncoder[A]): JsonObject =
    encoder.encodeObject(wrappedEncodeable)
}
```

# Encoder examples

`circe/examples/IdCard`

# Decoding

`String -> Parser -> Json -> HCursor -> Decoder -> Data Model`

```scala
trait Parser {
  def parse(input: String): Either[ParsingFailure, Json]
  def decode[A: Decoder](input: String): Either[Error, A]
}

trait Decoder[A] {
  def apply(c: HCursor): Decoder.Result[A]
}
```

# Добавяне на circe към проект

```scala
val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
```

* `circe-core` - core data type and type classes
* `circe-generic` - uses Shapeless to auto-generate Decoder/Encoder for case classes.
* `circe-parser` - Parser type class for decoding JSON

# HTTP библиотеки

* [http4s](https://http4s.org/) - based on cats-effect and fs2 (streaming)
* [akka-http](https://doc.akka.io/docs/akka-http/current/index.html) -
  full server- and client-side HTTP stack on top of akka-actor and akka-stream
* [Play Framework](https://www.playframework.com/) - build on Akka
* [Scalatra](http://scalatra.org/) - influenced on Ruby’s Sinatra. "microframework"

# http4s

> Http applications are just a Kleisli function from a streaming request to a polymorphic effect of a streaming response.
  So what's the problem?


<div class="fragment">
Нека разгледаме как е изграден http4s
</div>


# Започваме просто

```scala

case class Request(
    method: Method,
    uri: Uri,
    // headers,
    // httpVersion,
    body: EntityBody
)

case class Response(
    status: Status,
    // headers,
    // httpVersion,
    body: EntityBody
)

```

#

```scala
type HttpApp = Request => IO[Response]

type HttpRoutes = Request => IO[Option[Response]]
object HttpRoutes {
  def of(pf: PartialFunction[Request, IO[Response]]): HttpRoutes = pf.lift
}
```

<div class="fragment">

```scala
type Http[F[_]] = Request => F[Response]
type HttpApp = Http[IO]

type HttpRoutes = Http[IO[Option[_]]]
```

</div>

#

```scala
type Http[F[_]] = Request => F[Response]
type HttpApp = Http[IO]

type OptionIO[A] = IO[Option[A]]
type HttpRoutes = Http[OptionIO]
```


# [OptionT](https://typelevel.org/cats/datatypes/optiont.html)

> OptionT[F[_], A] is a light wrapper on an F[Option[A]]

<div class="fragment">

```scala
import cats.data.OptionT

type Http[F[_]] = Request => F[Response]
type HttpApp = Http[IO]

type OptionTIO[A] = OptionT[IO, A]
type HttpRoutes = Http[OptionTIO] 
```

</div>

<div class="fragment">

[Kind Projector](https://github.com/typelevel/kind-projector)
```scala
type HttpRoutes = Http[OptionT[IO, *]]
```

</div>

# Какво имаме накрая

```scala
type Http[F[_]] = Request => F[Response]

type HttpApp = Http[IO]

type HttpRoutes = Http[OptionT[IO, *]]
object HttpRoutes {
  def of(pf: PartialFunction[Request, IO[Response]]): HttpRoutes =
    req => OptionT(pf.lift(req).sequence)
}
```

# Всъщност е малко по-различно

[Kleisli](https://typelevel.org/cats/datatypes/kleisli.html)
```scala
type Http[F[_]] = Kleisli[F, Request, Response]
```

<div class="fragment">
Освен това - IO не е фиксирано

```scala
type HttpApp[F[_]] = Http[F]

type HttpRoutes[F[_]] = Http[OptionT[F, *]]
object HttpRoutes {
  def of[F[_]: Defer: Applicative](pf: PartialFunction[Request[F], F[Response[F]]]): HttpRoutes[F] =
    Kleisli(req => OptionT(Defer[F].defer(pf.lift(req).sequence)))
}
```

</div>

# Examples

# middlewares

* [документация](https://http4s.org/v0.23/middleware/)
* [Authentication](https://http4s.org/v0.23/auth/)
* Cross Origin Resource Sharing ([CORS](https://http4s.org/v0.23/cors))
* Response Compression ([GZip](https://http4s.org/v0.23/gzip))
* [Metrics](https://http4s.org/v0.23/api/org/http4s/server/middleware/Metrics) 
* [X-Request-ID header](https://http4s.org/v0.23/api/org/http4s/server/middleware/RequestId)

# http client

* Освен сървърна част, `http4s` има и HTTP клиент
* [пример от документацията](https://http4s.org/v0.23/client/)





