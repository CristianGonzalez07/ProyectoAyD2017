CREATE TABLE users (
  id  int(11) auto_increment PRIMARY KEY,
  username VARCHAR(128) NOT NULL,
  password VARCHAR(128) NOT NULL,
  permissions ENUM('SI','NO') Default 'NO',
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

CREATE TABLE questions (
  tag VARCHAR(128) PRIMARY KEY,
  category 	ENUM('CIENCIAS','DEPORTE','ENTRETENIMIENTO','GEOGRAFIA','HISTORIA'),
  question VARCHAR(128) NOT NULL,
  option1  VARCHAR(20) NOT NULL,
  option2  VARCHAR(20) NOT NULL,
  option3  VARCHAR(20) NOT NULL,
  option4  VARCHAR(20) NOT NULL,
  correct	ENUM('1','2','3','4'),	
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;
