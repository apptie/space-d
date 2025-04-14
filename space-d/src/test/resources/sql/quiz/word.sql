INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(1, now(), now(), 0, 'DEVELOP', 'Authorization', 0, '인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(1, now(), now(), '어써라이제이션', 'KOREAN', 1, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(1, now(), now(), '게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.', 1, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(2, now(), now(), 0, 'DEVELOP', 'YAML', 0, '사람이 읽기 쉬운 데이터 형식으로, 주로 설정 파일에 사용됩니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(2, now(), now(), '야믈', 'KOREAN', 2, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(2, now(), now(), 'YAML은 설정 파일이나 데이터 교환 포맷으로 자주 사용됩니다.', 2, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(3, now(), now(), 0, 'DEVELOP', 'TOML', 0, '간단하고 가독성이 높은 설정 파일 형식으로, 키-값 쌍을 이용해 데이터를 표현합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(3, now(), now(), '톰엘', 'KOREAN', 3, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(3, now(), now(), 'TOML은 구성 파일에 사용하기 쉬운 데이터 직렬화 언어입니다.', 3, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(4, now(), now(), 0, 'DEVELOP', 'deprecated', 0, '더 이상 사용되지 않거나, 지원되지 않는다는 뜻입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(4, now(), now(), '데프리케이티드', 'KOREAN', 4, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(4, now(), now(), '이 함수는 더 이상 사용되지 않으므로 deprecated되었습니다.', 4, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(5, now(), now(), 0, 'DEVELOP', 'execute', 0, '주로 프로그램이나 코드, 명령을 실행할 때 사용됩니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(5, now(), now(), '엑시큐트', 'KOREAN', 5, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(5, now(), now(), '코드를 실행하려면 Run 버튼을 눌러서 execute시킵니다.', 5, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(6, now(), now(), 0, 'DEVELOP', 'COALESCE', 0, 'SQL에서 인자로 주어진 컬럼들 중에서 NULL이 아닌 첫 번째 값을 반환하는 함수입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(6, now(), now(), '코얼레스', 'KOREAN', 6, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(6, now(), now(), '데이터베이스 쿼리에서 COALESCE 함수를 사용해 NULL 값을 빈 문자열로 대체합니다.', 6, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(7, now(), now(), 0, 'DEVELOP', 'Queue', 0, '대기열을 의미하며, 데이터 구조에서 먼저 들어온 데이터가 먼저 나가는(FIFO) 방식의 대기열을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(7, now(), now(), '큐', 'KOREAN', 7, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(7, now(), now(), '비동기 작업을 처리하기 위해 작업 Queue를 사용하여 작업을 순차적으로 실행합니다.', 7, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(8, now(), now(), 0, 'DEVELOP', 'carousel', 0, '회전목마를 의미하며, UI 중 이미지를 순환하며 보여주는 슬라이더를 지칭합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(8, now(), now(), '캐러셀', 'KOREAN', 8, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(8, now(), now(), '사용자는 이미지 캐러셀을 통해 다양한 사진을 볼 수 있습니다.', 8, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(9, now(), now(), 0, 'DEVELOP', 'Dequeue', 0, 'Queue의 반대 동작으로, 큐에 저장된 데이터 중 첫 번째 요소를 제거하고 반환하는 것을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(9, now(), now(), '디큐', 'KOREAN', 9, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(9, now(), now(), '사용자 요청을 Queue에 저장하고, 순서대로 Dequeue하여 처리합니다.', 9, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(10, now(), now(), 0, 'DEVELOP', 'JWT', 0, 'JWT는 JSON Web Token의 약자로, JSON 형식의 웹 토큰을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(10, now(), now(), '제이더블유티', 'KOREAN', 10, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(10, now(), now(), '사용자가 로그인하면 서버는 JWT를 생성하여 클라이언트에게 반환합니다.', 10, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(11, now(), now(), 0, 'DEVELOP', 'GUI', 0, '그래픽 사용자 인터페이스를 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(11, now(), now(), '구이', 'KOREAN', 11, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(11, now(), now(), '새로 출시된 운영 체제는 직관적인 GUI를 제공하여 사용자가 쉽게 파일을 관리하고 프로그램을 실행할 수 있습니다.', 11, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(12, now(), now(), 0, 'DEVELOP', 'usage', 0, '사용, 용법을 뜻합니다. CPU, 메모리, 네트워크 등 시스템이나 소프트웨어 자원의 사용량을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(12, now(), now(), '유씨지', 'KOREAN', 12, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(12, now(), now(), '시스템의 CPU usage가 80%를 초과했습니다.', 12, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(13, now(), now(), 0, 'DEVELOP', 'SaaS', 0, 'Software as a Service의 약자로, 서비스형 소프트웨어를 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(13, now(), now(), '사스', 'KOREAN', 13, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(13, now(), now(), '회사에 클라우드 기반 SaaS 솔루션을 도입했습니다.', 13, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(14, now(), now(), 0, 'DEVELOP', 'directory', 0, '파일 시스템에서 파일과 폴더를 계층적으로 구성하는 데 사용되는 구조를 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(14, now(), now(), '디렉터리', 'KOREAN', 14, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(14, now(), now(), 'directory 권한 설정을 통해 특정 사용자만 접근할 수 있도록 했습니다.', 14, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(15, now(), now(), 0, 'DEVELOP', 'empty', 0, '비어 있는, 아무것도 없는 상태를 의미하며, 변수나 데이터 구조가 값을 포함하지 않은 상태를 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(15, now(), now(), '엠티', 'KOREAN', 15, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(15, now(), now(), '배열이 empty인지 확인한 후에 데이터 추가 작업을 수행합니다.', 15, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(16, now(), now(), 0, 'DEVELOP', 'redirect', 0, '웹 서버나 애플리케이션에서 사용자가 요청한 URL을 다른 URL로 자동으로 보내는 행위를 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(16, now(), now(), '리다이렉트', 'KOREAN', 16, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(16, now(), now(), 'HTTP 상태 코드 중 301은 영구적으로 다른 URL로 redirect하며, 302는 일시적으로 다른 URL로 redirect했다는 뜻입니다.', 16, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(17, now(), now(), 0, 'DEVELOP', 'jar', 0, '자바 애플리케이션을 패키징하여 배포하는 데 사용되는 파일 형식으로, 여러 클래스 파일과 관련 메타데이터를 포함합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(17, now(), now(), '자르', 'KOREAN', 17, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(17, now(), now(), 'JAR 파일을 실행하여 애플리케이션을 시작합니다', 17, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(18, now(), now(), 0, 'DEVELOP', 'locale', 0, '소프트웨어나 시스템에서 특정 언어와 문화권에 맞춘 설정을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(18, now(), now(), '로캘', 'KOREAN', 18, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(18, now(), now(), '시스템의 locale을 한국어로 설정합니다.', 18, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(19, now(), now(), 0, 'DEVELOP', 'gradient', 0, 'CSS에서 색상이나 밝기가 점진적으로 변하는 효과로, 웹 페이지의 배경이나 요소에 주로 적용합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(19, now(), now(), '그레이디언트', 'KOREAN', 19, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(19, now(), now(), 'CSS에서 linear-gradient와 radial-gradient 속성을 사용하여 다양한 형태의 gradient를 만들 수 있습니다.', 19, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(20, now(), now(), 0, 'DEVELOP', 'status', 0, '시스템, 프로세스, 작업, 또는 소프트웨어의 현재 상태나 진행 상황을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(20, now(), now(), '스테이터스', 'KOREAN', 20, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(20, now(), now(), '클라이언트의 요청이 성공적으로 처리되었음을 나타내기 위해 서버는 200 OK HTTP status 코드를 반환합니다.', 20, false);

INSERT INTO word_randoms(id, category, random, word_id) VALUES(1, 'DEVELOP', 1, 1);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(2, 'DEVELOP', 2, 2);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(3, 'DEVELOP', 3, 3);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(4, 'DEVELOP', 4, 4);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(5, 'DEVELOP', 5, 5);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(6, 'DEVELOP', 6, 6);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(7, 'DEVELOP', 7, 7);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(8, 'DEVELOP', 8, 8);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(9, 'DEVELOP', 9, 9);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(10, 'DEVELOP', 10, 10);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(11, 'DEVELOP', 11, 11);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(12, 'DEVELOP', 12, 12);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(13, 'DEVELOP', 13, 13);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(14, 'DEVELOP', 14, 14);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(15, 'DEVELOP', 15, 15);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(16, 'DEVELOP', 16, 16);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(17, 'DEVELOP', 17, 17);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(18, 'DEVELOP', 18, 18);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(19, 'DEVELOP', 19, 19);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(20, 'DEVELOP', 20, 20);

UPDATE word_metadata SET develop_word_count = 20, total_word_count = 20 WHERE id = 1;
