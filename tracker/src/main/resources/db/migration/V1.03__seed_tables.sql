INSERT INTO users (auth_user_id, email, username, first_name, last_name, phone, role, active, created_at, updated_at)
VALUES ('demo-auth-user-id', 'test1@test.com', 'Tester', 'Test', 'User', '011-12345467', 'ROLE_USER', true, NOW(),
        NOW());

INSERT INTO categories (user_id, name, type, color, icon, created_at, updated_at)
VALUES (1, 'Food', 'EXPENSE', 'default', 'Utensils', NOW(), NOW()),
       (1, 'Transport', 'EXPENSE', 'default', 'Bus', NOW(), NOW()),
       (1, 'Shopping', 'EXPENSE', 'default', 'ShoppingBag', NOW(), NOW()),
       (1, 'Entertainment', 'EXPENSE', 'default', 'Gamepad', NOW(), NOW()),
       (1, 'Bills', 'EXPENSE', 'default', 'ReceiptCent', NOW(), NOW()),
       (1, 'Salary', 'INCOME', 'default', 'BriefcaseBusiness', NOW(), NOW()),
       (1, 'Electronics', 'EXPENSE', 'default', 'Cable', NOW(), NOW()),
       (1, 'Investments', 'INCOME', 'default', 'DollarSign', NOW(), NOW()),
       (1, 'Bonus', 'INCOME', 'default', 'HandCoins', NOW(), NOW());

INSERT INTO transactions (user_id, category, note, amount, type, date, remarks, created_at, updated_at)
VALUES (1, 'Food', 'Lunch', 8.50, 'EXPENSE', NOW(), 'Nasi goreng biasa', NOW(), NOW()),
       (1, 'Transport', 'Grab Ride', 22.00, 'EXPENSE', NOW(), 'From ABC to EFD', NOW(), NOW()),
       (1, 'Salary', 'Monthly Salary', 5000.00, 'INCOME', NOW(), 'Salary May', NOW(), NOW());