ALTER TABLE system_categories
    ADD CONSTRAINT uk_system_category_name_type UNIQUE (name, type);

ALTER TABLE system_categories_preferences
    ADD INDEX idx_system_category_preferences_user_id (user_id);

ALTER TABLE system_categories_preferences
    ADD CONSTRAINT uk_system_category_preferences_user UNIQUE (user_id);

ALTER TABLE system_categories_preferences
    ADD CONSTRAINT fk_system_category_preferences_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE;