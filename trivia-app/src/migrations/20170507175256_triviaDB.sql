CREATE TABLE IF NOT EXISTS users(
  id int(11) auto_increment PRIMARY KEY,
  username VARCHAR(128) NOT NULL,
  password VARCHAR(128) NOT NULL,
  permissions ENUM('YES','NO') DEFAULT 'NO',
  score int(11) NOT NULL DEFAULT 0,
  currentGame int(11),
  UNIQUE (username),	
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS questions(
  id int(11) auto_increment PRIMARY KEY,
  category 	ENUM('CIENCIAS','DEPORTES','ENTRETENIMIENTO','GEOGRAFIA','HISTORIA'),
  description VARCHAR(128) NOT NULL,
  option1  VARCHAR(50) NOT NULL,
  option2  VARCHAR(50) NOT NULL,
  option3  VARCHAR(50) NOT NULL,
  option4  VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS games(
	id int(11) auto_increment PRIMARY KEY,
	typeOfGame ENUM('1PLAYER','2PLAYER'),
	player1 VARCHAR(128) NOT NULL,
	player2 VARCHAR(128),
	scorePlayer1 int(11) NOT NULL DEFAULT 0,
	scorePlayer2 int(11) NOT NULL DEFAULT 0,
	moves int(11)  NOT NULL DEFAULT 0,
	status ENUM('WAITING','INPROGRESS','TERMINATED','UNINITIATED'),
	initiated TIMESTAMP,
	created_at TIMESTAMP NULL,
	updated_at TIMESTAMP NULL
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS invitations(
	id int(11) auto_increment PRIMARY KEY,
	origin VARCHAR(128) NOT NULL,
	destination VARCHAR(128) NOT NULL,
	accepted BOOLEAN,
	created_at TIMESTAMP NULL,
  	updated_at TIMESTAMP NULL
)ENGINE=InnoDB;
