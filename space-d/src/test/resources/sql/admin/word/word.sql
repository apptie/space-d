INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(1, now(), now(), 0, 'DEVELOP', 'Authorization', 0, '인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(1, now(), now(), '어써라이제이션', 'KOREAN', 1, false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(2, now(), now(), '오써러제이션', 'KOREAN', 1, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(1, now(), now(), '게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.', 1, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(2, now(), now(), '이번에 추가된 API 엔드포인트는 Authorization 체크가 제대로 들어갔나요?', 1, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(2, now(), now(), 0, 'DEVELOP', 'YAML', 0, '사람이 읽기 쉬운 데이터 형식으로, 주로 설정 파일에 사용됩니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(3, now(), now(), '야믈', 'KOREAN', 2, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(3, now(), now(), 'YAML은 설정 파일이나 데이터 교환 포맷으로 자주 사용됩니다.', 2, false);

INSERT INTO word_randoms(id, category, random, word_id) VALUES(1, 'DEVELOP', 1, 1);

INSERT INTO word_randoms(id, category, random, word_id) VALUES(2, 'DEVELOP', 2, 2);

UPDATE word_metadata SET develop_word_count = 2, total_word_count = 2 WHERE id = 1;
