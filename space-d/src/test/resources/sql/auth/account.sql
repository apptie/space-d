INSERT INTO accounts(id, created_at, updated_at, deleted, nickname, role, registration_id, social_identifier)
VALUES (1, now(), now(), false, '재빠른지구001', 'ROLE_USER', 'KAKAO', '12345');

UPDATE nickname_metadata SET total_count = 1 WHERE nickname = '재빠른지구';
