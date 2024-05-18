-- Insert sample data into the user_data table
INSERT INTO users (id, created_at, created_at_nano, updated_at, updated_at_nano, email, password, first_name, last_name,
                   email_verification_status, photo)
VALUES ('00000000-0000-0000-0000-000000000001', '2024-05-08 12:00:00', 1, '2024-05-08 12:00:00', 2, 'john@example.com',
        'password123', 'John', 'Doe', 'VERIFIED', '{"bucketName": "mybucket", "objectName": "myobjectkey"}'),
       ('00000000-0000-0000-0000-000000000002', '2024-05-08 12:00:00', 3, '2024-05-08 12:00:00', 4, 'mike@example.com',
        'password12345', 'Mike', 'Smith', 'UNVERIFIED', null),
       ('00000000-0000-0000-0000-000000000003', '2024-05-08 12:00:00', 5, '2024-05-08 12:00:00', 6, 'bob@example.com',
        'password12345', 'Bob', 'Dirkovich', 'UNVERIFIED', '{"bucketName": "mybucket1", "objectName": "myobjectkey2"}');

