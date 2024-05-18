--liquibase formatted sql
--changeset ritik:System-setup
--comment: system-setup

INSERT INTO `system_config`
(`id`, `created_at`, `created_by`, `deleted`, `modified_at`, `modified_by`, `config_key`, `config_value`)
VALUES
(1, '2024-05-07 05:29:17', 'ritik', 0, '2024-05-07 05:29:17', 'ritik', 'sms.otp.expiry.minutes', '5'),
(2, '2024-05-07 05:29:24', 'ritik', 0, '2024-05-07 05:29:24', 'ritik', 'delivery.charge', '10'),
(3, '2024-05-07 05:29:35', 'ritik', 0, '2024-05-07 05:29:35', 'ritik', 'cancelable.period', '30'),
(4, '2024-05-07 05:29:35', 'ritik', 0, '2024-05-07 05:29:35', 'ritik', 'imagekit.config', '');