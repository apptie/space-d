INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(2, now(), now(), 0, 'DEVELOP', 'HTTP', 0, 'HyperText Transfer Protocol의 약자.', true);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(2, now(), now(), '에이치티티피', 'KOREAN', 2, true);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(2, now(), now(), 'HTTP 통신이 정상적으로 수행되지 않는 것 같습니다.', 2, true);
