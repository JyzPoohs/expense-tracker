CREATE TABLE IF NOT EXISTS budgets
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT         NOT NULL,
    category_id BIGINT         NOT NULL,
    amount      DECIMAL(15, 2) NOT NULL,
    month       INT            NOT NULL,
    year        INT            NOT NULL,
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_budgets_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_budgets_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT uk_budget_user_category_month_year UNIQUE (user_id, category_id, month, year)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;