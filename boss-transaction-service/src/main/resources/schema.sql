create schema if not exists boss;
use boss;
CREATE TABLE IF NOT EXISTS transaction_type (
    id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name CHAR(36) NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    type_id TINYINT UNSIGNED NOT NULL,
    account_id LONG NOT NULL,
    overdraft_id INT UNSIGNED NULL,
    atm_transaction_id INT UNSIGNED NULL,
    merchant_name VARCHAR(64) NOT NULL,
    amount FLOAT NOT NULL,
    new_balance FLOAT NOT NULL,
    date TIMESTAMP NOT NULL,
    succeeded BIT NOT NULL,
    pending BIT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (type_id) REFERENCES transaction_type (id)
);