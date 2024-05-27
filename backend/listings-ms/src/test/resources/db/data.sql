-- Insert sample data into the user_data table
INSERT INTO listings (id, created_at, created_at_nano, updated_at, updated_at_nano, owner_id, title, description, status, photos)
VALUES ('00000000-0000-0000-0000-000000000001', '2024-05-08 12:00:00', 1, '2024-05-08 12:00:00', 2, '00000000-0000-0000-0000-000000000010',
        'listing1', null, 'DRAFT', null),
       ('00000000-0000-0000-0000-000000000002', '2024-05-08 12:00:00', 3, '2024-05-08 12:00:00', 4, '00000000-0000-0000-0000-000000000020',
        'listing2',  null, 'PUBLISHED', null);

