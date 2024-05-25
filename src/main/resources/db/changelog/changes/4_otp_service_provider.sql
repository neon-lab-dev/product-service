--liquibase formatted sql
--changeset ritik:otp-service-provider
--comment: setting up TeleSign


INSERT INTO `system_config`
(`id`, `created_at`, `created_by`, `deleted`, `modified_at`, `modified_by`, `config_key`, `config_value`)
VALUES
(5, '2024-05-07 05:29:17', 'ritik', 0, '2024-05-07 05:29:17', 'ritik', 'otp.provider.config'
, '{
       "customerId" : "C7E8EFDE-4EE9-4B72-9F72-3A290029C682",
       "apiKey" : "ae1alJtLoscigyy6PMMtul6tvJtSLLDi3otXdSL8WvLqF1jH8ohsdIDlxKms/WwlsqNJr4mMy58oFp8cJFZxkg=="
}');
