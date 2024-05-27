--liquibase formatted sql
--changeset ritik:otp-service-provider
--comment: setting up 2factor


INSERT INTO `system_config`
(`id`, `created_at`, `created_by`, `deleted`, `modified_at`, `modified_by`, `config_key`, `config_value`)
VALUES
(5, '2024-05-07 05:29:17', 'ritik', 0, '2024-05-07 05:29:17', 'ritik', 'otp.provider.config'
, '{
          "apiKey" : "e33c7a87-1c16-11ef-8b60-0200cd936042",
          "voiceOtpUrl" : "https://2factor.in/API/V1/%s/SMS/+91%s/%s"
 }');
