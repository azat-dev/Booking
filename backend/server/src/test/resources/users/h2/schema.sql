CREATE TABLE users
(
    id                        UUID PRIMARY KEY,
    created_at                TIMESTAMP    NOT NULL,
    updated_at                TIMESTAMP    NOT NULL,
    email                     VARCHAR(255) NOT NULL,
    encoded_password          VARCHAR(255) NOT NULL,
    first_name                VARCHAR(255) NOT NULL,
    last_name                 VARCHAR(255) NOT NULL,
    email_verification_status VARCHAR(50)  NOT NULL
);