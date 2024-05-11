CREATE TABLE outbox_events
(
    event_order  BIGINT       NOT NULL AUTO_INCREMENT,
    event_id     VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    event_type   VARCHAR(50)  NOT NULL,
    payload      TEXT         NOT NULL,
    is_published BOOLEAN      NOT NULL,
    PRIMARY KEY (event_id)
);


CREATE TABLE users
(
    id                        UUID PRIMARY KEY,
    created_at                TIMESTAMP    NOT NULL,
    updated_at                TIMESTAMP    NOT NULL,
    email                     VARCHAR(255) NOT NULL,
    password          VARCHAR(255) NOT NULL,
    first_name                VARCHAR(255) NOT NULL,
    last_name                 VARCHAR(255) NOT NULL,
    email_verification_status VARCHAR(50)  NOT NULL
);