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

INSERT INTO user (username, password, password_date, role)  VALUES
	('INewton', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('MCurie', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('AEinstein', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('LPasteur', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('GGalilei', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('CDarwin', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('NCopernico', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('MFaraday', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('AFleming', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('LBeethoven', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','ACCOUNTHOLDER'),
    ('Amazon', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','THIRDPARTY'),
    ('Apple', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','THIRDPARTY'),
    ('BSantander', '$2a$10$kSHH79NEXWTMdbQlAFxRUe.CVACVu5rfqOLWuzDWpRE6F7ig7OoBW','2021-09-23','THIRDPARTY')
;

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

INSERT INTO account_holder (user_id, name, nif, date_of_birth, street, city, country, postal_code, mailing_address)  VALUES
	(2,'Isaac Newton', '00000002Z','1975-09-23','Gravity Street', 'London', 'United Kingdom', 20505,'isaacnewton@gmail.com'),
    (3,'Marie Curie', '00000003Z','1985-10-15','Radius Street', 'Varsovia', 'Poland', 30949,'mariecurie@gmail.com'),
    (4,'Albert Einstein', '00000004Z','1966-03-12','Realativity Street', 'Ulm','Germany', 16496,'alberteinstein@gmail.com'),
    (5,'Louis Pasteur', '00000005Z','2000-11-23','Vaccine Street', 'Dole','France', 91616,'louispasteur@gmail.com'),
    (6,'Galileo Galilei', '00000006Z','1982-09-23','Astronomy Street', 'Pisa','Italy', 41518,'galileogalilei@gmail.com'),
    (7,'Charles Darwin', '00000007Z','1985-05-21','Evolution Street', 'Shrewsbury','United Kingdom', 64813,'charlesdarwin@gmail.com'),
    (8,'Nicolás Copérnico', '00000008Z','2005-06-23','Heliocentrism Street', 'Torun','Poland', 70693,'nicolascopernico@gmail.com'),
    (9,'Michael Faraday', '00000009Z','1956-09-07','Electromagnetism Street', 'Newington','United Kingdom', 16516,'michaelfaraday@gmail.com'),
    (10,'Alexander Fleming', '000000010Z','1977-09-23','Medicine Street', 'Darve','United Kingdom', 20564,'alexanderfleming@gmail.com'),
    (11,'Ludwig van Beethoven', '000000011Z','1999-02-23','Music Street', 'Bonn','Germany', 22654,'ludwingvanbethoven@gmail.com')
;

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

INSERT INTO third_party (user_id, name, hashed_key)  VALUES
	(12,'Amazon', 'amazon_123'),
    (13,'Apple', 'apple_123'),
    (14,'Banco Santander', 'bsantander_123')
;

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

SELECT * FROM user;
SELECT * FROM accounts;

