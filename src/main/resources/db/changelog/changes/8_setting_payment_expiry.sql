--liquibase formatted sql
--changeset ritik:payment-link-expiry
--comment: setting up RazorPay payment link expiry

INSERT INTO `system_config`
(`id`, `created_at`, `created_by`, `deleted`, `modified_at`, `modified_by`, `config_key`, `config_value`)
VALUES
(7, '2024-05-07 05:29:17', 'ritik', 0, '2024-05-07 05:29:17', 'ritik', 'payment.expire.after.minutes', '16');