-- 사용자 알림 설정 테이블
CREATE TABLE notification_settings
(
    notification_setting_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id               BIGINT       NOT NULL UNIQUE,
    is_enabled              BOOLEAN      NOT NULL DEFAULT TRUE,
    reminders_enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    reminder_time           TIME         NOT NULL DEFAULT '21:00:00',
    risk_warnings_enabled   BOOLEAN      NOT NULL DEFAULT TRUE,
    news_updates_enabled    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_date            DATETIME     NOT NULL,
    update_date             DATETIME,
    FOREIGN KEY (member_id) REFERENCES members (member_id)
);

-- 기존 모든 사용자에 대해 기본 알림 설정을 추가합니다.
INSERT INTO notification_settings (member_id, created_date, update_date)
SELECT member_id, NOW(), NOW() FROM members;
