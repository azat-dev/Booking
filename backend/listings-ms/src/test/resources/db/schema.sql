CREATE TABLE IF NOT EXISTS listings
(
    id              UUID PRIMARY KEY,
    created_at      TIMESTAMP    NOT NULL,
    created_at_nano INTEGER      NOT NULL,
    updated_at      TIMESTAMP    NOT NULL,
    updated_at_nano INTEGER      NOT NULL,
    owner_id        UUID         NOT NULL,
    title           VARCHAR(255) NOT NULL UNIQUE,
    description     TEXT         NULL,
    status          VARCHAR(50)  NOT NULL,
    photos          JSON         NULL
);