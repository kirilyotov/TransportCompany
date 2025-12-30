--liquibase formatted sql

--changeset ${AUTHOR_NAME}:1.5_create_cargo_table
CREATE TABLE IF NOT EXISTS  cargo (
    cargo_id        INT AUTO_INCREMENT PRIMARY KEY,
    transport_id    INT NOT NULL,
    type            ENUM('goods', 'passengers') NOT NULL,
    description     VARCHAR(255),
    total_weight    DECIMAL(8, 2),
    passenger_count INT       DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (transport_id) REFERENCES transports (transport_id) ON DELETE CASCADE,
    CHECK (
        (type = 'goods' AND total_weight > 0 AND passenger_count = 0)
            OR
        (type = 'passengers' AND passenger_count > 0 AND total_weight IS NULL)
        )
);