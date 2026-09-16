ALTER TABLE budgets RENAME COLUMN amount TO budget;

ALTER TABLE budgets MODIFY COLUMN category_id BIGINT NULL;

ALTER TABLE budgets
    ADD CONSTRAINT uk_budgets_user_category_period UNIQUE (user_id, category_id, month, year)