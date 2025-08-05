INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(1, now(), now(), 0, 'DEVELOP', 'Authorization', 0, '인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(1, now(), now(), '어써라이제이션', 'KOREAN', 1, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(1, now(), now(), '게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.', 1, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(2, now(), now(), 0, 'DEVELOP', 'annotation', 0, '소스 코드에 추가되는 주석이나 설명을 의미하며, 코드의 이해를 돕기 위해 사용됩니다.', true);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(2, now(), now(), '어노테이션', 'KOREAN', 2, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(2, now(), now(), '어노테이션은 소스 코드에 주석을 추가하여 코드의 의미를 명확히 하는 데 사용됩니다.', 2, false);

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

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(21, now(), now(), 0, 'DEVELOP', 'digital', 0, '디지털 기술과 관련된 용어로, 아날로그가 아닌 이진 데이터를 사용하는 방식을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(21, now(), now(), '디지털', 'KOREAN', 21, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(21, now(), now(), 'digital 신호 처리는 아날로그 신호를 디지털로 변환하는 과정입니다.', 21, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(22, now(), now(), 0, 'DEVELOP', 'debugging', 0, '프로그램의 오류를 찾아 수정하는 과정입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(22, now(), now(), '디버깅', 'KOREAN', 22, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(22, now(), now(), '코드를 debugging하여 문제를 해결했습니다.', 22, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(23, now(), now(), 0, 'DEVELOP', 'disk', 0, '컴퓨터 저장 장치 중 하나로, 데이터를 저장하는 물리적 매체입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(23, now(), now(), '디스크', 'KOREAN', 23, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(23, now(), now(), 'disk 용량이 부족하여 파일을 삭제했습니다.', 23, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(24, now(), now(), 0, 'DEVELOP', 'directory', 0, '파일 시스템에서 파일과 폴더를 계층적으로 구성하는 구조입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(24, now(), now(), '디렉토리', 'KOREAN', 24, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(24, now(), now(), 'directory 권한을 설정하여 접근을 제한했습니다.', 24, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(25, now(), now(), 0, 'DEVELOP', 'device', 0, '하드웨어 장치를 의미하며, 컴퓨터 주변기기를 포함합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(25, now(), now(), '디바이스', 'KOREAN', 25, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(25, now(), now(), '새로운 device를 연결하여 테스트했습니다.', 25, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(26, now(), now(), 0, 'DEVELOP', 'design', 0, '제품이나 서비스의 외관 및 기능을 계획하고 설계하는 과정입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(26, now(), now(), '디자인', 'KOREAN', 26, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(26, now(), now(), '웹사이트 design을 새롭게 변경했습니다.', 26, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(27, now(), now(), 0, 'DEVELOP', 'default', 0, '기본 설정이나 초기값을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(27, now(), now(), '디폴트', 'KOREAN', 27, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(27, now(), now(), 'default 설정으로 프로그램을 실행했습니다.', 27, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(28, now(), now(), 0, 'DEVELOP', 'decode', 0, '암호화된 데이터를 해독하는 과정을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(28, now(), now(), '디코딩', 'KOREAN', 28, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(28, now(), now(), '비디오 스트림을 decode하여 재생했습니다.', 28, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(29, now(), now(), 0, 'DEVELOP', 'debounce', 0, '입력 신호의 잡음을 제거하는 기술입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(29, now(), now(), '디바운스', 'KOREAN', 29, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(29, now(), now(), '버튼 클릭 시 debounce 처리를 적용했습니다.', 29, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(30, now(), now(), 0, 'DEVELOP', 'display', 0, '화면에 정보를 출력하는 장치나 기능을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(30, now(), now(), '디스플레이', 'KOREAN', 30, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(30, now(), now(), '고해상도 display를 사용하여 화면을 선명하게 표현했습니다.', 30, false);
