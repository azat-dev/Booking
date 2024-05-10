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
