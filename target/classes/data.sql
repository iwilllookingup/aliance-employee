DROP TABLE IF EXISTS employee;

CREATE TABLE employee
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(128) NOT NULL,
    surname  VARCHAR(128) NOT NULL,
    phone    VARCHAR(32)  NOT NULL,
    position VARCHAR(64) DEFAULT NULL
);

INSERT INTO employee (name, surname, phone, position)
VALUES ('Chakrit', 'Sereepong', '0888888888', 'developer'),
       ('Kittisak', 'Tongdee', '0999999999', 'software engineer');


DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    username VARCHAR(128) PRIMARY KEY,
    password VARCHAR(256) NOT NULL,
);