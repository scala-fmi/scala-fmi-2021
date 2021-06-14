package fmi.infrastructure.db

case class DbConfig(host: String,
                    port: Int,
                    user: String,
                    password: String,
                    name: String,
                    schema: String,
                    connectionPoolSize: Int) {
  def jdbcUrl: String = s"jdbc:postgresql://$host:$port/$name"
}
