CREATE TABLE users (
  id  int(11) auto_increment PRIMARY KEY,
  username  VARCHAR(128),
  password  VARCHAR(128),
  permissions ENUM('SI','NO') Default 'NO',
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

CREATE TABLE questions (
  name  VARCHAR(128) NOT NULL PRIMARY KEY,
  category 	ENUM('Ciencias','Deporte','Entretenimiento','Geografia','Historia'),
  question  VARCHAR(128),
  option1  VARCHAR(20),
  option2  VARCHAR(20),
  option3  VARCHAR(20),
  option4  VARCHAR(20),
  correct	ENUM('1','2','3','4'),	
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;