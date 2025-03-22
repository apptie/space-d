SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE accounts;
TRUNCATE TABLE bookmarks;
TRUNCATE TABLE comments;
TRUNCATE TABLE quiz_graded_answers;
TRUNCATE TABLE likes;
TRUNCATE TABLE nickname_metadata;
TRUNCATE TABLE pronunciations;
TRUNCATE TABLE quiz_metadata;
TRUNCATE TABLE quiz_options;
TRUNCATE TABLE quiz_questions;
TRUNCATE TABLE quizzes;
TRUNCATE TABLE reports;
TRUNCATE TABLE skills;
TRUNCATE TABLE today_quizzes;
TRUNCATE TABLE today_quiz_graded_answer;
TRUNCATE TABLE today_quiz_options;
TRUNCATE TABLE word_examples;
TRUNCATE TABLE word_metadata;
TRUNCATE TABLE word_randoms;
TRUNCATE TABLE words;

SET REFERENTIAL_INTEGRITY TRUE;
