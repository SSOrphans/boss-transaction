CREATE TABLE IF NOT EXISTS transaction (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    account_id INT UNSIGNED NOT NULL,
    overdraft_id INT UNSIGNED NULL,
    atm_transaction_id INT UNSIGNED NULL,
    merchant_name VARCHAR(64) NOT NULL,
    amount FLOAT NOT NULL,
    new_balance FLOAT NOT NULL,
    date TIMESTAMP NOT NULL,
    succeeded BIT NOT NULL,
    pending BIT NOT NULL,
    PRIMARY KEY (id)
);