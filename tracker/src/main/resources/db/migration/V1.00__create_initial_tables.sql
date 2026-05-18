CREATE TABLE IF NOT EXISTS users
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    auth_user_id VARCHAR(255) NOT NULL UNIQUE,
    email        VARCHAR(255) NOT NULL UNIQUE,
    username     VARCHAR(100) NOT NULL UNIQUE,
    full_name    VARCHAR(255),
    currency     VARCHAR(10)  NOT NULL DEFAULT 'MYR',
    theme        VARCHAR(20)  NOT NULL DEFAULT 'light',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS categories
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    type       VARCHAR(20)  NOT NULL,
    color      VARCHAR(20),
    icon       VARCHAR(50),
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uk_category_user_name_type UNIQUE (user_id, name, type)
);

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