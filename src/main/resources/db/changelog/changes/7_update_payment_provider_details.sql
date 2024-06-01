--liquibase formatted sql
--changeset ritik:order-url
--comment: replacing payment link with order url

UPDATE `system_config`
SET
    `config_value` = '{
                             "apiKeyId" : "rzp_test_GG81ZocPpvSUO0",
                             "apiKeySecret" : "uEaxVpixhRAZlKOQcsLE19KM",
                             "orderBaseUrl" : "https://api.razorpay.com/v1/orders"
                         }'
WHERE
    `id` = 6;