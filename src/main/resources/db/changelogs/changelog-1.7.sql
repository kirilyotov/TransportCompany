--liquibase formatted sql

--changeset ${AUTHOR_NAME}:1.7_create_transport_status_table
CREATE TABLE IF NOT EXISTS  transport_status (
    status_id     INT AUTO_INCREMENT PRIMARY KEY,
    transport_id  INT      NOT NULL,
    status_change DATETIME NOT NULL,
    old_status    VARCHAR(50),
    new_status    VARCHAR(50),
    notes         TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (transport_id) REFERENCES transports (transport_id) ON DELETE CASCADE
);