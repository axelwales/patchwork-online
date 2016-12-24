CREATE TABLE Users (
	id NUMBER,
	username VARCHAR2(20) NOT NULL UNIQUE,
	password VARCHAR2(70) NOT NULL,
	email VARCHAR2(50),
	PRIMARY KEY (id)
);

CREATE TABLE User_Roles (
	username VARCHAR2(20) NOT NULL,
	role VARCHAR2(20) NOT NULL,
	PRIMARY KEY (username, role),
	FOREIGN KEY (username) REFERENCES Users(username)
);

CREATE TABLE Games (
	id NUMBER,
	name VARCHAR2(30) UNIQUE,
	isstarted NUMBER,
	iscomplete NUMBER,
	boardbonus NUMBER,
	PRIMARY KEY (id) 
);
CREATE TABLE Players (
	id NUMBER,
	username VARCHAR2(20) NOT NULL,
	gameid NUMBER,
	income NUMBER,
	position NUMBER,
	buttons NUMBER,
	currentplayer NUMBER,
	isai NUMBER,
	boardbonus NUMBER,
	board VARCHAR2(200),
	PRIMARY KEY (id),
	FOREIGN KEY (gameid) REFERENCES Games,
	FOREIGN KEY (username) REFERENCES Users(username)
);
CREATE TABLE Actions (
	id NUMBER,
	playerid NUMBER,
	gameid NUMBER,
	position NUMBER,
	ordernumber NUMBER,
	iscomplete NUMBER,
	PRIMARY KEY (id),
	FOREIGN KEY (gameid) REFERENCES Games 
);
CREATE TABLE Commands (
	id NUMBER,
	actionid NUMBER,
	name VARCHAR2(50),
	params VARCHAR2(50),
	position NUMBER,
	iscomplete NUMBER,
	PRIMARY KEY (id),
	FOREIGN KEY (actionid) REFERENCES Actions 
);
CREATE TABLE Patches (
	id NUMBER,
	patchid NUMBER,
	PRIMARY KEY (id)
);
CREATE TABLE GamePatches (
	id NUMBER,
	gameid NUMBER,
	patchid NUMBER,
	position NUMBER,
	waschosen NUMBER,
	ordernumber NUMBER,
	PRIMARY KEY (id),
	FOREIGN KEY (gameid) REFERENCES Games,
	FOREIGN KEY (patchid) REFERENCES Patches(id)
);
CREATE TABLE PlayerPatches (
	id NUMBER,
	playerid NUMBER,
	patchid NUMBER,
	columnnumber NUMBER,
	rownumber NUMBER,
	PRIMARY KEY (id),
	FOREIGN KEY (playerid) REFERENCES Players,
	FOREIGN KEY (patchid) REFERENCES Patches(id)
);
CREATE TABLE TimeTracks (
	id NUMBER,
	gameid NUMBER,
	nextbonus NUMBER,
	claimedbonuses VARCHAR2(200),
	PRIMARY KEY (id),
	FOREIGN KEY (gameid) REFERENCES Games
);

CREATE SEQUENCE id_seq START WITH 1;