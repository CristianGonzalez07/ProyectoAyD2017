CREATE TABLE users (
  id  int(11) auto_increment PRIMARY KEY,
  username NOT NULL VARCHAR(128),
  password NOT NULL VARCHAR(128),
  permissions ENUM('SI','NO') Default 'NO',
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

CREATE TABLE questions (
  name VARCHAR(128) NOT NULL PRIMARY KEY,
  category 	ENUM('Ciencias','Deporte','Entretenimiento','Geografia','Historia'),
  question NOT NULL VARCHAR(128),
  option1  NOT NULL VARCHAR(20),
  option2  NOT NULL VARCHAR(20),
  option3  NOT NULL VARCHAR(20),
  option4  NOT NULL VARCHAR(20),
  correct	ENUM('1','2','3','4'),	
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;
