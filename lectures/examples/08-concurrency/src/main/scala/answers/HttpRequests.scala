package answers

import http.HttpClient
import util.HttpServiceUrls

object HttpRequests extends App {
  import concurrent.Executors.defaultExecutor

  val result = for {
    (a, b) <- HttpClient.get("https://google.com").zip(HttpClient.get("https://www.scala-lang.org/"))

    aLength = a.getResponseBody.length
    bLength = b.getResponseBody.length
    sum = aLength + bLength

    randomNumber <- HttpClient.get(HttpServiceUrls.randomNumberUpTo(sum))
  } yield randomNumber.getResponseBody

  result.foreach(println)
}
