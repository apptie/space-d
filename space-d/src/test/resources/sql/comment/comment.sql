INSERT INTO comments(id, created_at, updated_at, content, deleted, like_count, word_id, writer_id)
VALUES (1, now(), now(), '이 용어는 언제 쓰는건가요?', false, 0, 1, 1);

INSERT INTO comments(id, created_at, updated_at, content, deleted, like_count, word_id, writer_id)
VALUES (2, now(), now(), '이 용어 쓰는걸 본 적이 없는거 같아요', true, 1, 1, 1);
