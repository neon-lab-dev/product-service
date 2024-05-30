--liquibase formatted sql
--changeset ritik:adding-password
--comment: Adding password for admin


UPDATE auth_user
SET
    `password` = 'test'
WHERE
    `id` = '075922e2-7c69-417f-815f-1f4178095frk';