package fmi.user

import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {
  def hash(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
  def checkPasswords(password: String, passwordHash: String): Boolean = BCrypt.checkpw(password, passwordHash)
}
