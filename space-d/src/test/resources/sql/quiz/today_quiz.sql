INSERT INTO today_quizzes(id, created_at, question, question_content, quiz_category, content, word_id) VALUES(1, now(), '다음 예문을 보고 예문에 맞는 용어를 선택해주세요.', '인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘', 'DEVELOP', 'Authorization', 1);

INSERT INTO today_quiz_options(id, content, option_order, word_id, today_quiz_id) VALUES(1, 'Authorization', 1, 1, 1);
INSERT INTO today_quiz_options(id, content, option_order, word_id, today_quiz_id) VALUES(2, 'YAML', 2, 2, 1);
INSERT INTO today_quiz_options(id, content, option_order, word_id, today_quiz_id) VALUES(3, 'TOML', 3, 3, 1);
INSERT INTO today_quiz_options(id, content, option_order, word_id, today_quiz_id) VALUES(4, 'deprecated', 4, 4, 1);
