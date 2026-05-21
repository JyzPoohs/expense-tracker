INSERT INTO users (auth_user_id, email, username, first_name, last_name, phone, role, active, created_at, updated_at)
VALUES ('demo-auth-user-id', 'test1@test.com', 'Tester', 'Test', 'User', '011-12345467', 'ROLE_USER', true, NOW(),
        NOW());

INSERT INTO system_categories (name, type, color, icon, is_active, created_at, updated_at)
VALUES ('Food', 'EXPENSE', 'default', 'Utensils', true, NOW(), NOW()),
       ('Transport', 'EXPENSE', 'default', 'Bus', true, NOW(), NOW()),
       ('Shopping', 'EXPENSE', 'default', 'ShoppingBag', true, NOW(), NOW()),
       ('Entertainment', 'EXPENSE', 'default', 'Gamepad', true, NOW(), NOW()),
       ('Bills', 'EXPENSE', 'default', 'ReceiptCent', true, NOW(), NOW()),
       ('Salary', 'INCOME', 'default', 'BriefcaseBusiness', true, NOW(), NOW()),
       ('Electronics', 'EXPENSE', 'default', 'Cable', true, NOW(), NOW()),
       ('Investments', 'INCOME', 'default', 'DollarSign', true, NOW(), NOW()),
       ('Bonus', 'INCOME', 'default', 'HandCoins', true, NOW(), NOW());

INSERT INTO categories (user_id, name, type, color, icon, is_active, created_at, updated_at)
VALUES (1, 'Pet', 'EXPENSE', 'default', 'Cat', true, NOW(), NOW()),
       (1, 'Rental', 'EXPENSE', 'default', 'House', true, NOW(), NOW()),
       (1, 'AngPau', 'Income', 'default', 'PiggyBank', true, NOW(), NOW()),
       (1, 'Medicine', 'EXPENSE', 'default', 'BriefcaseMedical', true, NOW(), NOW());

INSERT INTO system_categories_preferences (user_id, preferences, created_at,  updated_at)
VALUES (1, '{  "hiddenCategories": [1, 4],  "customColors": {  "2": "#00ff00", "5": "#0000ff"  }  }',   NOW(), NOW());

INSERT INTO transactions (user_id, category, note, amount, type, date, remarks, created_at, updated_at)
VALUES (1, 'Food', 'Lunch', 8.50, 'EXPENSE', NOW(), 'Nasi goreng biasa', NOW(), NOW()),
       (1, 'Transport', 'Grab Ride', 22.00, 'EXPENSE', NOW(), 'From ABC to EFD', NOW(), NOW()),
       (1, 'Salary', 'Monthly Salary', 5000.00, 'INCOME', NOW(), 'Salary May', NOW(), NOW());