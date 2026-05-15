CREATE TABLE IF NOT EXISTS users
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    keycloak_user_id VARCHAR(255) NOT NULL UNIQUE,
    email            VARCHAR(255) NOT NULL UNIQUE,
    username         VARCHAR(100) NOT NULL UNIQUE,
    full_name        VARCHAR(255),
    currency         VARCHAR(10)  NOT NULL DEFAULT 'MYR',
    timezone         VARCHAR(100) NOT NULL DEFAULT 'Asia/Kuala_Lumpur',
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE categories
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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'transaction id',
    user_id     BIGINT         NOT NULL,
    category_id BIGINT         NOT NULL,
    description VARCHAR(255)   NOT NULL COMMENT 'description',
    amount      DECIMAL(15, 2) NOT NULL COMMENT 'amount',
    type        VARCHAR(20)    NOT NULL COMMENT 'type：INCOME/EXPENSE/TRANSFER',
    date        DATETIME       NOT NULL COMMENT 'transaction date',
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_transactions_user_id ON transactions (user_id);
CREATE INDEX idx_transactions_category_id ON transactions (category_id);
CREATE INDEX idx_transactions_transaction_date ON transactions (date);