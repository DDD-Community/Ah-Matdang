UPDATE notification_settings
SET reminder_time = '15:00:00';

ALTER TABLE notification_settings
    ALTER COLUMN reminder_time SET DEFAULT '15:00:00';
