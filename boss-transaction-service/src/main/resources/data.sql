INSERT INTO transaction_type (id, name) VALUES (1, 'TRANSACTION_DEPOSIT');
INSERT INTO transaction_type (id, name) VALUES (2, 'TRANSACTION_WITHDRAW');
INSERT INTO transaction_type (id, name) VALUES (3, 'TRANSACTION_TRANSFER');
INSERT INTO transaction_type (id, name) VALUES (4, 'TRANSACTION_PAYMENT');
INSERT INTO transaction_type (id, name) VALUES (5, 'TRANSACTION_CHECK');
INSERT INTO transaction_type (id, name) VALUES (6, 'TRANSACTION_ATM_DEPOSIT');
INSERT INTO transaction_type (id, name) VALUES (7, 'TRANSACTION_ATM_WITHDRAW');

INSERT INTO transaction (id, type_id, account_id, overdraft_id, atm_transaction_id, merchant_name, amount, new_balance, date, succeeded, pending)
VALUES (1, 1, 1, null, null, 'TestMerchant', 123.45, 12345.67, NOW(), true, false);

