
CREATE TABLE cities (
  id   INT(5) AUTO_INCREMENT NOT NULL,
  name VARCHAR(50) UNIQUE    NOT NULL,
  country VARCHAR(3),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE TABLE accounts (
  id          INT AUTO_INCREMENT NOT NULL,
  city_id     INT(5)             NOT NULL,
  temp        DECIMAL(10, 2)     NOT NULL,
  time        TIMESTAMP          NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (city_id) REFERENCES cities (id),
)
  ENGINE = InnoDB;


