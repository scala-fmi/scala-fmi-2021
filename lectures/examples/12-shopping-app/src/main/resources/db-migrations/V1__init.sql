CREATE TABLE "user" (
  email TEXT PRIMARY KEY,
  password_hash TEXT NOT NULL,
  role TEXT NOT NULL,
  name TEXT NOT NULL,
  age INT
);
