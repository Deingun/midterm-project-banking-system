CREATE SCHEMA banking_system_new;
CREATE SCHEMA banking_system_new_test;

USE banking_system_new;
USE banking_system_new_test;

DROP TABLE IF EXISTS user;

CREATE TABLE user(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
username VARCHAR(255) NOT NULL,
password VARCHAR(255),
password_date DATE NOT NULL,
role VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS role;

CREATE TABLE role (
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
name VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS user_role;

CREATE TABLE user_role (
user_id BIGINT  NOT NULL,
role_id BIGINT  NOT NULL,
PRIMARY KEY(user_id,role_id)
);

DROP TABLE IF EXISTS account_holder;

CREATE TABLE account_holder(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
user_id BIGINT NOT NULL,
name VARCHAR(10),
nif VARCHAR(10),
date_of_birth DATE,
street VARCHAR(255),
city VARCHAR(255),
country VARCHAR(255),
postal_code INT,
mailing_address VARCHAR(255),
FOREIGN KEY (user_id) REFERENCES user(id)
);

DROP TABLE IF EXISTS admin;

CREATE TABLE admin(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
user_id BIGINT NOT NULL,
name VARCHAR(10),
FOREIGN KEY (user_id) REFERENCES user(id)
);

DROP TABLE IF EXISTS third_party;

CREATE TABLE third_party(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
user_id BIGINT NOT NULL,
name VARCHAR(10),
hashed_key VARCHAR(25),
FOREIGN KEY (user_id) REFERENCES user(id)
);

DROP TABLE IF EXISTS checking_account;

CREATE TABLE checking_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity_number VARCHAR(5),
branch_number VARCHAR(5),
account_number VARCHAR(25),
balance DECIMAL,
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
penalty_fee DECIMAL,
secret_key VARCHAR(255),
minimum_balance DECIMAL,
monthly_maintenance_fee DECIMAL,
creation_date DATE,
status VARCHAR(255),
FOREIGN KEY (primary_owner_id) REFERENCES account_holder (user_id),
FOREIGN KEY (secondary_owner_id) REFERENCES account_holder (user_id)
);

INSERT INTO checking_account(id, entity_number,branch_number,balance,primary_owner_id,secondary_owner_id,penalty_fee,secret_key,minimum_balance,monthly_maintenance_fee,creation_date) VALUES
(1, '0049','1500',1000,119,120,40,'abc',100,10,'2021-09-11');



DROP TABLE IF EXISTS student_checking_account;

CREATE TABLE student_checking_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity_number VARCHAR(5),
branch_number VARCHAR(5),
account_number VARCHAR(25),
balance DECIMAL,
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
penalty_fee DECIMAL,
secret_key VARCHAR(255),
creation_date DATE,
status VARCHAR(255),
FOREIGN KEY (primary_owner_id) REFERENCES account_holder (user_id),
FOREIGN KEY (secondary_owner_id) REFERENCES account_holder (user_id)
);

DROP TABLE IF EXISTS saving_account;

CREATE TABLE saving_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity_number VARCHAR(5),
branch_number VARCHAR(5),
account_number VARCHAR(25),
balance DECIMAL,
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
penalty_fee DECIMAL,
secret_key VARCHAR(255),
minimum_balance DECIMAL,
creation_date DATE,
status VARCHAR(255),
interest_rate FLOAT,
FOREIGN KEY (primary_owner_id) REFERENCES account_holder (user_id),
FOREIGN KEY (secondary_owner_id) REFERENCES account_holder (user_id)
);

DROP TABLE IF EXISTS credit_card_account;

CREATE TABLE credit_card_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity_number VARCHAR(5),
branch_number VARCHAR(5),
account_number VARCHAR(25),
balance DECIMAL,
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
penalty_fee DECIMAL,
credit_limit DECIMAL,
interest_rate FLOAT,
FOREIGN KEY (primary_owner_id) REFERENCES account_holder (user_id),
FOREIGN KEY (secondary_owner_id) REFERENCES account_holder (user_id)
);

select * from user;
select * from account_holder;
select * from admin;
select * from role;
select * from role;
select * from checking_account;

select u.username, a.nif from user u left join account_holder a on u.id = a.user_id where a.nif is NOT NULL;












