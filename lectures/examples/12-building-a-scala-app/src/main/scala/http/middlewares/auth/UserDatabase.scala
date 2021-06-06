package http.middlewares.auth

import cats.effect.IO

object UserDatabase {
    val users = Map(
      1 -> User(1, "Viktor"),
      2 -> User(2, "Zdravko"),
      3 -> User(3, "Boyan")
    )

    def apply(id: Int): IO[Option[User]] = IO.pure(users.get(id))
  }
