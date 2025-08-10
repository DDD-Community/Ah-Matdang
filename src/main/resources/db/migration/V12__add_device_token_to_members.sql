-- members 테이블에 FCM 디바이스 토큰 컬럼 추가
ALTER TABLE members
    ADD COLUMN device_token VARCHAR(255) NULL;
