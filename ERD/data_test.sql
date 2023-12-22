SELECT *
  FROM user;

SELECT *
  FROM post;

SELECT *
  FROM post_comment;
 
 SELECT *
  FROM user_authority;
 
 SELECT *
  FROM user_authorities;
 
 SELECT *
 FROM user_img;
 
SELECT *
FROM post_img;

SELECT *
FROM post;

SELECT *
FROM recommend;

INSERT INTO post(userId, title, content)
SELECT userId, title, content FROM post;


SELECT count(*) 
FROM post p INNER JOIN user u
ON p.userId = u.id
WHERE p.title LIKE '%마라탕%'
OR u.nickName like '%마라탕%';