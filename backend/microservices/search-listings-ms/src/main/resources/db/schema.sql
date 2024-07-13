CREATE TABLE IF NOT EXISTS outbox_events
(
    event_order    BIGSERIAL    NOT NULL,
    event_id       VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    created_at_nano INTEGER      NOT NULL,
    event_type     VARCHAR(50)  NOT NULL,
    payload        TEXT         NOT NULL,
    is_published   BOOLEAN      NOT NULL,
    tracing_info   JSON         NULL,
    PRIMARY KEY (event_id, event_type)
);

CREATE TABLE IF NOT EXISTS listings
(
    id                       UUID PRIMARY KEY,
    created_at               TIMESTAMP    NOT NULL,
    created_at_nano          INTEGER      NOT NULL,
    updated_at               TIMESTAMP    NOT NULL,
    updated_at_nano          INTEGER      NOT NULL,
    host_id                 UUID         NOT NULL,
    title                    VARCHAR(255) NOT NULL,
    description              TEXT         NULL,
    photos                   JSON         NULL,

    guests_capacity_adults   INTEGER      NOT NULL,
    guests_capacity_children INTEGER      NOT NULL,
    guests_capacity_infants  INTEGER      NOT NULL,
    property_type            VARCHAR(50)  NULL,
    room_type                VARCHAR(50)  NULL,
    address_street           VARCHAR(255) NULL,
    address_city             VARCHAR(255) NULL,
    address_country          VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS available_dates
(
    id                       UUID PRIMARY KEY,
    listing_id               UUID         NOT NULL,
    start_date               DATE         NOT NULL,
    end_date                 DATE         NOT NULL,
    price                    INTEGER      NOT NULL,
    currency                 VARCHAR(50)  NOT NULL,
    price_usd                FLOAT        NOT NULL
);