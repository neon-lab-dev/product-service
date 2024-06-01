--liquibase formatted sql
--changeset ritik:webhook-config
--comment: setting up RazorPay webhook security

ALTER TABLE
    `system_config`
MODIFY COLUMN
    `config_value` TEXT;

UPDATE `system_config`
SET
    `config_value` = '{
                             "apiKeyId" : "rzp_test_GG81ZocPpvSUO0",
                             "apiKeySecret" : "uEaxVpixhRAZlKOQcsLE19KM",
                             "paymentLinkBaseUrl" : "https://api.razorpay.com/v1/payment_links",
                             "webhookSecret" : "kaserapay"
                         }'
WHERE
    `id` = 6;
