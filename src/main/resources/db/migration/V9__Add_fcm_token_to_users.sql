-- V9__Add_fcm_token_to_users.sql
-- Add FCM token column for push notifications

ALTER TABLE users ADD COLUMN IF NOT EXISTS fcm_token VARCHAR(500);

COMMENT ON COLUMN users.fcm_token IS 'Firebase Cloud Messaging token for push notifications';
