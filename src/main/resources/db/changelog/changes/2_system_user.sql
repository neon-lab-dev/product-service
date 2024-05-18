--liquibase formatted sql
--changeset ritik:dummy system user
--comment: default system user


INSERT INTO `user`
(`id`,`created_at`,`created_by`,`deleted`,`modified_at`,`modified_by`,`email`,`name`,`primary_phone_no`,`secondary_phone_no`)
VALUES
('bd9b5ab5-839a-400e-b337-ebe1e62ad1ee','2024-05-13 06:17:59.512000','9999999999',0,'2024-05-13 06:17:59.512000','9999999999','system@neonshark.in','System','9999999999',NULL);

INSERT INTO auth_user
(id,created_at,created_by,deleted,modified_at,modified_by,active,roles,token,user_id,user_name)
VALUES
('075922e2-7c69-417f-815f-1f4178095f27','2024-05-13 06:17:59.528000','9999999999',0,'2024-05-13 06:17:59.544000','9999999999',1,'ADMIN,USER','02cce0b9-1541-4e22-b5c8-6ef422d8f622','bd9b5ab5-839a-400e-b337-ebe1e62ad1ee','9999999999');
