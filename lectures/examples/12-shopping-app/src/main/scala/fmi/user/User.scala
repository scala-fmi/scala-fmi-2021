package fmi.user

case class User(
  id: UserId,
  passwordHash: String,
  role: UserRole.Value,
  name: String,
  age: Option[Int]
)

case class UserId(email: String) extends AnyVal

object UserRole extends Enumeration {
  val Admin, NormalUser = Value
}
