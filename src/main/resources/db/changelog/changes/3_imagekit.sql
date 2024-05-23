--liquibase formatted sql
--changeset ritik:setting-imagekit
--comment: setting up imagekit


UPDATE `system_config`
SET
	`config_value` = '{
    "urlEndpoint" : "https://ik.imagekit.io/vnpzydnah",
    "privateKey" : "private_y3G6TTQ3k+d6zd4DKXP0Sf2KonE=",
    "publicKey" : "public_XLohLdaFLUOTq25k2/E+Guvyog4="
    }'
WHERE
	`id` = 4;
