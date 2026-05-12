CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'transaction id',
    description VARCHAR(255) NOT NULL COMMENT 'description',
    amount DECIMAL(10, 2) NOT NULL COMMENT 'amount',
    type VARCHAR(20) NOT NULL COMMENT 'type：INCOME/EXPENSE/TRANSFER',
    category VARCHAR(50) NOT NULL COMMENT 'category',
    date DATETIME NOT NULL COMMENT 'date',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_transaction_date ON transactions(date);
CREATE INDEX idx_transaction_type ON transactions(type);
CREATE INDEX idx_transaction_category ON transactions(category);