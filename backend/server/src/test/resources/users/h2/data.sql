-- Insert sample data into the user_data table
INSERT INTO users (id, created_at, updated_at, email, encoded_password, first_name, last_name,
                       email_verification_status)
VALUES ('00000000-0000-0000-0000-000000000001', '2024-05-08 12:00:00', '2024-05-08 12:00:00', 'john@example.com',
        'password123', 'John', 'Doe', 'VERIFIED');
