--liquibase formatted sql
--changeset ritik:payment-service-provider
--comment: setting up RazorPay


INSERT INTO `system_config`
(`id`, `created_at`, `created_by`, `deleted`, `modified_at`, `modified_by`, `config_key`, `config_value`)
VALUES
(6, '2024-05-07 05:29:17', 'ritik', 0, '2024-05-07 05:29:17', 'ritik', 'payment-service.provider.config'
, '{
       "apiKeyId" : "rzp_test_GG81ZocPpvSUO0",
       "apiKeySecret" : "uEaxVpixhRAZlKOQcsLE19KM",
       "paymentLinkBaseUrl" : "https://api.razorpay.com/v1/payment_links"
   }');
