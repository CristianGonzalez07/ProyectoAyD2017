CREATE TABLE users (
  id  int(11) auto_increment PRIMARY KEY, -- DEFAULT NULL tira error
  username  VARCHAR(128),
  password  VARCHAR(128),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;