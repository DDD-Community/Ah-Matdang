-- 회원 탈퇴 기능을 위한 deleted_at 컬럼 추가
ALTER TABLE members ADD COLUMN deleted_at DATE NULL;

-- deleted_at 컬럼에 인덱스 추가 (배치 작업 성능 향상)
CREATE INDEX idx_members_deleted_at ON members(deleted_at);