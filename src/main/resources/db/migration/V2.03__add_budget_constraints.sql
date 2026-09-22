ALTER TABLE budgets RENAME COLUMN budget TO amount;

ALTER TABLE budgets
    ADD CONSTRAINT fk_budgets_category
        FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE;