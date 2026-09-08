CREATE TABLE IF NOT EXISTS transactions
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'transaction id',
    user_id    BIGINT         NOT NULL,
    category   VARCHAR(255)   NOT NULL,
    note       VARCHAR(255)   NOT NULL COMMENT 'note',
    amount     DECIMAL(15, 2) NOT NULL COMMENT 'amount',
    type       VARCHAR(20)    NOT NULL COMMENT 'type：INCOME/EXPENSE/TRANSFER',
    date       DATETIME       NOT NULL COMMENT 'transaction date',
    remarks    VARCHAR(255)   NOT NULL COMMENT 'remarks',
    created_at DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_transactions_user_id ON transactions (user_id);
CREATE INDEX idx_transactions_transaction_date ON transactions (date);