-- Insert sample data into the user_data table
INSERT INTO listings (id,
                      created_at, created_at_nano,
                      updated_at, updated_at_nano,
                      host_id,
                      status,
                      title,
                      description,
                      property_type,
                      room_type,
                      photos,
                      guests_capacity_adults, guests_capacity_children, guests_capacity_infants,
                      address_street, address_city, address_country)
VALUES ('00000000-0000-0000-0000-000000000001',
        '2024-05-08 12:00:00', 1,
        '2024-05-08 12:00:00', 2,
        '00000000-0000-0000-0000-000000000010',
        'DRAFT',
        'listing1',
        'description1',
        'APARTMENT',
        'PRIVATE_ROOM',
        '[{"id": "photo1", "bucketName": "bucket1", "objectName": "object1"}]',
        1, 2, 3,
        'street1', 'city1', 'country1'),
       ('00000000-0000-0000-0000-000000000002',
        '2024-05-08 12:00:00', 3,
        '2024-05-08 12:00:00', 4,
        '00000000-0000-0000-0000-000000000020',
        'PUBLISHED',
        'listing2',
        null,
        'APARTMENT',
        'PRIVATE_ROOM',
        null,
        1, 0, 0,
        'street1', 'city1', 'country1'),
       ('00000000-0000-0000-0000-000000000003',
        '2024-05-08 12:00:00', 1,
        '2024-05-08 12:00:00', 2,
        '00000000-0000-0000-0000-000000000010',
        'DRAFT',
        'listing3',
        'description3',
        'APARTMENT',
        'PRIVATE_ROOM',
        null,
        1, 0, 0,
        'street1', 'city1', 'country1');

