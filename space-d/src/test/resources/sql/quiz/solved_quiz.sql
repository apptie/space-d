SET REFERENTIAL_INTEGRITY FALSE;

INSERT INTO quizzes(id, created_at, updated_at, account_id, solved) VALUES(1, now(), now(), 1, true);

INSERT INTO quiz_questions(id, question_content, question_example, content, word_id, quiz_category, quiz_id) VALUES(1, '다음 예문을 보고 예문에 맞는 용어를 선택해주세요.', '인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘', 'Authorization', 1, 'DEVELOP', 1);
INSERT INTO quiz_questions(id, question_content, question_example, content, word_id, quiz_category, quiz_id) VALUES(2, '다음 예문을 보고 예문에 맞는 용어를 선택해주세요.', '사람이 읽기 쉬운 데이터 형식으로, 주로 설정 파일에 사용', 'YAML', 2, 'DEVELOP', 1);
INSERT INTO quiz_questions(id, question_content, question_example, content, word_id, quiz_category, quiz_id) VALUES(3, '다음 예문을 보고 예문에 맞는 용어를 선택해주세요.', '간단하고 가독성이 높은 설정 파일 형식으로, 키-값 쌍을 이용해 데이터를 표현', 'TOML', 3, 'DEVELOP', 1);
INSERT INTO quiz_questions(id, question_content, question_example, content, word_id, quiz_category, quiz_id) VALUES(4, '다음 예문을 보고 예문에 맞는 용어를 선택해주세요.', '더 이상 사용되지 않거나, 지원되지 않는다는 뜻', 'deprecated', 4, 'DEVELOP', 1);
INSERT INTO quiz_questions(id, question_content, question_example, content, word_id, quiz_category, quiz_id) VALUES(5, '다음 예문을 보고 예문에 맞는 용어를 선택해주세요.', ' 주로 프로그램이나 코드, 명령을 실행할 때 사용', 'execute', 5, 'DEVELOP', 1);

INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(1, 'Authorization', 1, 1, 1);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(2, 'COALESCE', 2, 6, 1);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(3, 'Queue', 3, 7, 1);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(4, 'carousel', 4, 8, 1);

INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(5, 'Dequeue', 1, 9, 2);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(6, 'YAML', 2, 2, 2);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(7, 'JWT', 3, 10, 2);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(8, 'GUI', 4, 11, 2);

INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(9, 'usage', 1, 12, 3);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(10, 'SaaS', 2, 13, 3);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(11, 'TOML', 3, 3, 3);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(12, 'directory', 4, 14, 3);

INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(13, 'empty', 1, 15, 4);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(14, 'deprecated', 2, 4, 4);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(15, 'redirect', 3, 16, 4);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(16, 'jar', 4, 17, 4);

INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(17, 'execute', 1, 5, 5);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(18, 'execute', 2, 18, 5);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(19, 'execute', 3, 19, 5);
INSERT INTO quiz_options(id, content, option_order, word_id, quiz_question_id) VALUES(20, 'execute', 4, 20, 5);

SET REFERENTIAL_INTEGRITY TRUE;
