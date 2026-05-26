INSERT INTO users (auth_user_id, email, username, first_name, last_name, phone, role, active, created_at, updated_at)
VALUES ('demo-auth-user-id', 'test1@test.com', 'Tester', 'Test', 'User', '011-12345467', 'ROLE_USER', true, NOW(),
        NOW());

INSERT INTO system_categories (name, type, color, icon, is_active, is_system, created_at, updated_at)
VALUES ('Food', 'EXPENSE', 'default', 'Utensils', true, true, NOW(), NOW()),
       ('Transport', 'EXPENSE', 'default', 'Bus', true, true, NOW(), NOW()),
       ('Shopping', 'EXPENSE', 'default', 'ShoppingBag', true, true, NOW(), NOW()),
       ('Entertainment', 'EXPENSE', 'default', 'Gamepad', true, true, NOW(), NOW()),
       ('Bills', 'EXPENSE', 'default', 'ReceiptCent', true, true, NOW(), NOW()),
       ('Salary', 'INCOME', 'default', 'BriefcaseBusiness', true, true, NOW(), NOW()),
       ('Electronics', 'EXPENSE', 'default', 'Cable', true, true, NOW(), NOW()),
       ('Investments', 'INCOME', 'default', 'DollarSign', true, true, NOW(), NOW()),
       ('Bonus', 'INCOME', 'default', 'HandCoins', true, true, NOW(), NOW());

INSERT INTO categories (user_id, name, type, color, icon, is_active, is_system, created_at, updated_at)
VALUES (1, 'Pet', 'EXPENSE', 'default', 'Cat', true, false, NOW(), NOW()),
       (1, 'Rental', 'EXPENSE', 'default', 'House', true, false, NOW(), NOW()),
       (1, 'AngPau', 'Income', 'default', 'PiggyBank', true, false, NOW(), NOW()),
       (1, 'Medicine', 'EXPENSE', 'default', 'BriefcaseMedical', true, false, NOW(), NOW());

INSERT INTO system_categories_preferences (user_id, preferences, created_at, updated_at)
VALUES (1, '{  "hiddenCategories": [1, 4],  "customColors": {  "2": "#00ff00", "5": "#0000ff"  }  }', NOW(), NOW());

INSERT INTO transactions (user_id, category, note, amount, type, date, remarks, created_at, updated_at)
VALUES (1, 'Food', 'Lunch', 8.50, 'EXPENSE', NOW(), 'Nasi goreng biasa', NOW(), NOW()),
       (1, 'Transport', 'Grab Ride', 22.00, 'EXPENSE', NOW(), 'From ABC to EFD', NOW(), NOW()),
       (1, 'Salary', 'Monthly Salary', 5000.00, 'INCOME', NOW(), 'Salary May', NOW(), NOW());