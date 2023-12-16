drop table if exists emails;

CREATE TABLE `emails`
(
    email        nvarchar(255) DEFAULT NULL,
    updated_date datetime(6) DEFAULT NULL,
    PRIMARY KEY (email)
) ENGINE = InnoDB;