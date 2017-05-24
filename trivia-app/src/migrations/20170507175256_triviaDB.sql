CREATE TABLE IF NOT EXISTS users(
  id int(11) auto_increment PRIMARY KEY,
  username VARCHAR(128) NOT NULL,
  password VARCHAR(128) NOT NULL,
  permissions ENUM('YES','NO') Default 'NO',
  state	ENUM('ACTIVE','INACTIVE') Default 'INACTIVE',
  UNIQUE (username),	
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS questions(
  id int(11) auto_increment PRIMARY KEY,
  category 	ENUM('CIENCIAS','DEPORTE','ENTRETENIMIENTO','GEOGRAFIA','HISTORIA'),
  description VARCHAR(128) NOT NULL,
  option1  VARCHAR(20) NOT NULL,
  option2  VARCHAR(20) NOT NULL,
  option3  VARCHAR(20) NOT NULL,
  option4  VARCHAR(20) NOT NULL,
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS games(
  id int(11) auto_increment PRIMARY KEY,
  player1_id int(11) NOT NULL,
  player2_id int(11) NOT NULL,
  state	ENUM('IN_PROGRESS','FINISHIED'),
  scorep1 int(11),
  scorep2 int(11),
  turn ENUM('1','2'),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;
