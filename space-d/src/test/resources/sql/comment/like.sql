INSERT INTO likes(id, account_id, comment_id) VALUES(1, 2, 1);

UPDATE comments SET like_count = 1 WHERE id = 1;
