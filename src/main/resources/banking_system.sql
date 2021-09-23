CREATE SCHEMA banking_system_new;
CREATE SCHEMA banking_system_new_test;

USE banking_system_new;
USE banking_system_new_test;

DROP TABLE IF EXISTS user;

CREATE TABLE user(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
username VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255),
password_date DATE NOT NULL,
role VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS account_holder;

CREATE TABLE account_holder(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
user_id BIGINT NOT NULL,
name VARCHAR(255),
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
name VARCHAR(255),
FOREIGN KEY (user_id) REFERENCES user(id)
);

DROP TABLE IF EXISTS third_party;

CREATE TABLE third_party(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
user_id BIGINT NOT NULL,
name VARCHAR(255),
hashed_key VARCHAR(25),
FOREIGN KEY (user_id) REFERENCES user(id)
);

DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity_number VARCHAR(5),
branch_number VARCHAR(5),
account_number VARCHAR(25),
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
balance DECIMAL,
currency VARCHAR(5),
penalty_fee DECIMAL,
account_type VARCHAR(255),
FOREIGN KEY (primary_owner_id) REFERENCES account_holder (user_id),
FOREIGN KEY (secondary_owner_id) REFERENCES account_holder (user_id)
);

DROP TABLE IF EXISTS checking_account;

CREATE TABLE checking_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
account_id BIGINT,
secret_key VARCHAR(255),
minimum_balance DECIMAL,
monthly_maintenance_fee DECIMAL,
creation_date DATE,
status VARCHAR(255),
FOREIGN KEY (account_id) REFERENCES accounts (id)
);

DROP TABLE IF EXISTS student_checking_account;

CREATE TABLE student_checking_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
account_id BIGINT,
secret_key VARCHAR(255),
creation_date DATE,
status VARCHAR(255)
);

DROP TABLE IF EXISTS saving_account;

CREATE TABLE saving_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
account_id BIGINT,
secret_key VARCHAR(255),
minimum_balance DECIMAL,
creation_date DATE,
status VARCHAR(255),
interest_rate FLOAT,
last_interest_rate_date DATE
);

DROP TABLE IF EXISTS credit_card_account;

CREATE TABLE credit_card_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
account_id BIGINT,
credit_limit DECIMAL,
interest_rate FLOAT,
last_interest_rate_date DATE
);

DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
origin_account_id BIGINT,
destination_account_id BIGINT,
paymaster_id BIGINT,
receiver_id BIGINT,
amount DECIMAL,
currency VARCHAR(5),
time_stamp TIMESTAMP,
FOREIGN KEY (origin_account_id) REFERENCES accounts (id),
FOREIGN KEY (destination_account_id) REFERENCES accounts (id),
FOREIGN KEY (paymaster_id) REFERENCES user (id),
FOREIGN KEY (receiver_id) REFERENCES user (id)
);