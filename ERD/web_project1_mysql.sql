SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS user_authorities;
DROP TABLE IF EXISTS authority;
DROP TABLE IF EXISTS post_comment;
DROP TABLE IF EXISTS post_img;
DROP TABLE IF EXISTS recommend;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS user_img;
DROP TABLE IF EXISTS user;




/* Create Tables */

CREATE TABLE authority
(
	id int NOT NULL AUTO_INCREMENT,
	name varchar(40) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (name)
);


CREATE TABLE post
(
	id int NOT NULL AUTO_INCREMENT,
	title varchar(50) NOT NULL,
	content longtext NOT NULL,
	viewcnt int DEFAULT 0 DEFAULT 0,
	recommendcnt int DEFAULT 0,
	createDate datetime DEFAULT now(),
	user_id int NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE post_comment
(
	id int NOT NULL AUTO_INCREMENT,
	content text NOT NULL,
	createDate datetime DEFAULT now(),
	user_id int NOT NULL,
	post_id int NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE post_img
(
	id int NOT NULL AUTO_INCREMENT,
	sourceName varchar(100) NOT NULL,
	fileName varchar(100) NOT NULL,
	post_id int NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE recommend
(
	user_id int NOT NULL,
	post_id int NOT NULL
);


CREATE TABLE user
(
	id int NOT NULL AUTO_INCREMENT,
	username varchar(100) NOT NULL,
	password varchar(300) NOT NULL,
	nickname varchar(80) NOT NULL,
	name varchar(80) NOT NULL,
	email varchar(80) NOT NULL,
	regdate datetime DEFAULT now(),
	PRIMARY KEY (id),
	UNIQUE (username),
	UNIQUE (nickname),
	UNIQUE (email)
);


CREATE TABLE user_authorities
(
	user_id int NOT NULL,
	authority_id int NOT NULL
);


CREATE TABLE user_img
(
	id int NOT NULL AUTO_INCREMENT,
	sourceName varchar(100) NOT NULL,
	fileName varchar(100) NOT NULL,
	user_id int NOT NULL,
	PRIMARY KEY (id)
);



/* Create Foreign Keys */

ALTER TABLE user_authorities
	ADD FOREIGN KEY (authority_id)
	REFERENCES authority (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE post_comment
	ADD FOREIGN KEY (post_id)
	REFERENCES post (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE post_img
	ADD FOREIGN KEY (post_id)
	REFERENCES post (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE recommend
	ADD FOREIGN KEY (post_id)
	REFERENCES post (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE post
	ADD FOREIGN KEY (user_id)
	REFERENCES user (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE post_comment
	ADD FOREIGN KEY (user_id)
	REFERENCES user (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE recommend
	ADD FOREIGN KEY (user_id)
	REFERENCES user (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE user_authorities
	ADD FOREIGN KEY (user_id)
	REFERENCES user (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE user_img
	ADD FOREIGN KEY (user_id)
	REFERENCES user (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;



