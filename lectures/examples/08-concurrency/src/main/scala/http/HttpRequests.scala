package http

import util.HttpServiceUrls

object HttpRequests extends App {
  import concurrent.Executors.defaultExecutor

  val exampleRequest = HttpClient.get("http://example.com").map(_.getResponseBody)
  val googleRequest = HttpClient.get("http://google.com").map(_.getResponseBody)

  val result = for {
    (exampleResponse, googleResponse) <- exampleRequest zip googleRequest
    randomNumberUrl = HttpServiceUrls.randomNumberUpTo(exampleResponse.length + googleResponse.length)
    randomNumberResponse <- HttpClient.get(randomNumberUrl)
  } yield randomNumberResponse.getResponseBody

  result.foreach(println)
}
