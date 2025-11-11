-- Insert a test user for login testing
-- Password: password123 (will be hashed by BCrypt in the application)
-- This user can be used for testing the login functionality

-- Note: The password needs to be a BCrypt encoded version in production
-- For testing, insert a user and register through the app instead

-- Insert Test Customer User
INSERT INTO users (username, email, password, first_name, last_name, phone, role, created_at) 
VALUES (
  'testuser',
  'test@ecobazaarx.com',
  '$2a$12$WDGU7aLlYT2nJKLZHGZyuOnD/Wh6BX0ZLgXrRfBYELRpLvvQ4f6iC',  -- BCrypt hash of 'password123'
  'Test',
  'User',
  '9876543210',
  'USER',
  NOW()
);

-- Insert Test Admin User
INSERT INTO users (username, email, password, first_name, last_name, phone, role, created_at) 
VALUES (
  'admin',
  'admin@ecobazaarx.com',
  '$2a$12$WDGU7aLlYT2nJKLZHGZyuOnD/Wh6BX0ZLgXrRfBYELRpLvvQ4f6iC',  -- BCrypt hash of 'password123'
  'System',
  'Administrator',
  '9000000000',
  'ADMIN',
  NOW()
);

-- Insert Test Seller User
INSERT INTO users (username, email, password, first_name, last_name, phone, role, created_at) 
VALUES (
  'seller',
  'seller@ecobazaarx.com',
  '$2a$12$WDGU7aLlYT2nJKLZHGZyuOnD/Wh6BX0ZLgXrRfBYELRpLvvQ4f6iC',  -- BCrypt hash of 'password123'
  'Test',
  'Seller',
  '9111111111',
  'SELLER',
  NOW()
);

-- Login Credentials for Testing:
-- Username: testuser, Password: password123 (Role: USER)
-- Username: admin, Password: password123 (Role: ADMIN)
-- Username: seller, Password: password123 (Role: SELLER)

-- To view all users:
-- SELECT id, username, email, first_name, role FROM users;
