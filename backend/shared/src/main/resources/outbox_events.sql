CREATE TABLE IF NOT EXISTS outbox_events
(
    event_order    BIGSERIAL    NOT NULL,
    event_id       VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    created_at_nano INTEGER      NOT NULL,
    event_type     VARCHAR(50)  NOT NULL,
    payload        TEXT         NOT NULL,
    is_published   BOOLEAN      NOT NULL,
    PRIMARY KEY (event_id, event_type)
);