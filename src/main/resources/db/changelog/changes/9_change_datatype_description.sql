--liquibase formatted sql
--changeset ritik:update-datatype-description
--comment: Resolution for datatype issue

ALTER table `product` MODIFY COLUMN `description` TEXT;
ALTER table `variety` MODIFY COLUMN `description` TEXT;