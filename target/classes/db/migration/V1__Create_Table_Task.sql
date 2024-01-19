CREATE TABLE IF NOT EXISTS `tb_tasks`
(
    `id_product`  binary(16)   NOT NULL,
    `title`       varchar(40)  NOT NULL,
    `description` varchar(120) NOT NULL,
    `status`      BOOLEAN      NOT NULL,
    PRIMARY KEY (`id_product`)
);