CREATE TABLE product (
  sku TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT NOT NULL,
  weight_in_grams INT NOT NULL
);

CREATE TABLE product_stock (
  sku TEXT PRIMARY KEY,
  quantity INT NOT NULL
);
