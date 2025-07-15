-- Create databases if they don't exist
-- It's good practice to check existence before creating
CREATE DATABASE userservice;
CREATE DATABASE transactionservice;
CREATE DATABASE paymentservice;

-- You can also create specific users for each database if needed,
-- but for simplicity, using the main POSTGRES_USER is common in dev.
-- Example:
-- CREATE USER user_service_user WITH PASSWORD 'secure_password';
-- GRANT ALL PRIVILEGES ON DATABASE userdb TO user_service_user;