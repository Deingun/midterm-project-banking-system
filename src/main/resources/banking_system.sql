CREATE SCHEMA banking_system;
CREATE SCHEMA banking_system_test;

USE banking_system;
USE banking_system_test;

CREATE TABLE user(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
username VARCHAR(255),
password VARCHAR(255),
password_date DATE
);

CREATE TABLE role (
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
name VARCHAR(255),
user_id BIGINT,
FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE account_holder(
user_id BIGINT NOT NULL PRIMARY KEY,
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

CREATE TABLE admin(
user_id BIGINT NOT NULL PRIMARY KEY,
name VARCHAR(10),
FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE third_party(
user_id BIGINT NOT NULL PRIMARY KEY,
name VARCHAR(10),
hashed_key VARCHAR(25),
FOREIGN KEY (user_id) REFERENCES user(id)
);


CREATE TABLE checking_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity INT,
branch INT,
balance DECIMAL,
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
penalty_fee DECIMAL,
secret_key VARCHAR(255),
minimum_balance DECIMAL,
monthly_maintenance_fee DECIMAL,
creation_date DATE,
status VARCHAR(255),
FOREIGN KEY (primary_owner_id) REFERENCES user (id),
FOREIGN KEY (secondary_owner_id) REFERENCES user (id)
);

CREATE TABLE student_checking_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity INT,
branch INT,
balance DECIMAL,
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
penalty_fee DECIMAL,
secret_key VARCHAR(255),
creation_date DATE,
status VARCHAR(255),
FOREIGN KEY (primary_owner_id) REFERENCES user (id),
FOREIGN KEY (secondary_owner_id) REFERENCES user (id)
);

CREATE TABLE saving_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity INT,
branch INT,
balance DECIMAL,
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
penalty_fee DECIMAL,
secret_key VARCHAR(255),
minimum_balance DECIMAL,
creation_date DATE,
status VARCHAR(255),
interest_rate FLOAT,
FOREIGN KEY (primary_owner_id) REFERENCES user (id),
FOREIGN KEY (secondary_owner_id) REFERENCES user (id)
);

CREATE TABLE credit_card_account(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
entity INT,
branch INT,
balance DECIMAL,
primary_owner_id BIGINT,
secondary_owner_id BIGINT,
penalty_fee DECIMAL,
credit_limit DECIMAL,
interest_rate FLOAT,
FOREIGN KEY (primary_owner_id) REFERENCES user (id),
FOREIGN KEY (secondary_owner_id) REFERENCES user (id)
);












