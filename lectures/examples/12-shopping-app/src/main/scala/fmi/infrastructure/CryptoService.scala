package fmi.infrastructure

import org.reactormonk.{CryptoBits, PrivateKey}

class CryptoService(privateKey: String) {
  private val key = PrivateKey(scala.io.Codec.toUTF8(privateKey))
  private val crypto = CryptoBits(key)

  def encrypt(str: String): String = {
    crypto.signToken(str, System.currentTimeMillis().toString) // TODO: wrap it in referentially transparent IO
  }

  def decrypt(str: String): Option[String] = {
    crypto.validateSignedToken(str)
  }
}
