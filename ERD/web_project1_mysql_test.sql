-- 게시판에 대량의 데이터 입력
INSERT INTO post(user_id, title, content)
SELECT user_id, title, content FROM post;

SELECT * FROM user;

SELECT * FROM post;