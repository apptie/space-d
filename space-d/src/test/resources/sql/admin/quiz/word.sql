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
VALUES(5, now(), now(), 0, 'BUSINESS', 'ROI', 0, '투자 대비 얻은 수익의 비율을 계산하는 지표입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(5, now(), now(), '아르오아이', 'KOREAN', 5, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(5, now(), now(), '신제품 출시 ROI 분석 결과 150%의 효율성이 확인되었습니다.', 5, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(6, now(), now(), 0, 'BUSINESS', 'Burn Rate', 0, '스타트업이 매월 소비하는 현금의 양을 의미합니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(6, now(), now(), '번 레이트', 'KOREAN', 6, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(6, now(), now(), '현재 번 레이트가 월 5천만 원이라면 6개월 후 자본 고갈 위헙니다.', 6, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(7, now(), now(), 0, 'BUSINESS', 'KPI', 0, '조직의 목표 달성 정도를 측정하는 핵심 기준입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(7, now(), now(), '케이피아이', 'KOREAN', 7, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(7, now(), now(), '이번 분기 KPI로 고객 유지율 85%를 설정했습니다.', 7, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(8, now(), now(), 0, 'BUSINESS', 'Scrum', 0, '애자일 개발 방법론 중 반복적인 프로젝트 관리 기법입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(8, now(), now(), '스크럼', 'KOREAN', 8, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(8, now(), now(), '매일 15분 스크럼 미팅으로 작업 현황을 공유합니다.', 8, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(9, now(), now(), 0, 'DESIGN', 'Responsive Design', 0, '화면 크기에 따라 레이아웃이 자동으로 조절되는 디자인 기법입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(9, now(), now(), '반응형 디자인', 'KOREAN', 9, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(9, now(), now(), '모바일과 PC에서 모두 최적화된 반응형 디자인을 적용했습니다.', 9, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(10, now(), now(), 0, 'DESIGN', 'UX', 0, '제품 사용 시 사용자가 느끼는 종합적인 경험을 연구하는 분야입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(10, now(), now(), '유저 익스피리언스', 'KOREAN', 10, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(10, now(), now(), 'UX 개선을 위해 사용자 행동 패턴을 분석 중입니다.', 10, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(11, now(), now(), 0, 'DESIGN', 'A/B Testing', 0, '두 가지 디자인 버전을 비교해 효과를 측정하는 실험 방법입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(11, now(), now(), '에이비 테스팅', 'KOREAN', 11, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(11, now(), now(), '버튼 색상 변경 A/B 테스트에서 빨간색이 30% 더 클릭되었습니다.', 11, false);

INSERT INTO words(id, created_at, updated_at, bookmark_count, category, name, view_count, meaning, deleted)
VALUES(12, now(), now(), 0, 'DESIGN', 'Grid System', 0, '레이아웃을 구조화하기 위해 열과 행을 기준으로 요소를 배치하는 방식입니다.', false);

INSERT INTO pronunciations(id, created_at, updated_at, content, pronunciation_type, word_id, deleted)
VALUES(12, now(), now(), '그리드 시스템', 'KOREAN', 12, false);

INSERT INTO word_examples(id, created_at, updated_at, content, word_id, deleted)
VALUES(12, now(), now(), '그리드 시스템을 사용해 콘텐츠의 시각적 균형을 맞췄습니다.', 12, false);

INSERT INTO word_randoms(id, category, random, word_id) VALUES(1, 'DEVELOP', 1, 1);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(2, 'DEVELOP', 2, 2);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(3, 'DEVELOP', 3, 3);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(4, 'DEVELOP', 4, 4);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(5, 'BUSINESS', 5, 5);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(6, 'BUSINESS', 6, 6);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(7, 'BUSINESS', 7, 7);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(8, 'BUSINESS', 8, 8);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(9, 'DESIGN', 9, 9);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(10, 'DESIGN', 10, 10);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(11, 'DESIGN', 11, 11);
INSERT INTO word_randoms(id, category, random, word_id) VALUES(12, 'DESIGN', 12, 12);

UPDATE word_metadata SET develop_word_count = 4, business_word_count = 4, design_word_count = 4, total_word_count = 12 WHERE id = 1;
