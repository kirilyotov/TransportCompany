--liquibase formatted sql
--changeset ${AUTHOR_NAME}:1.8_status_trigger runOnChange:true splitStatements:false stripComments:false endDelimiter:;
CREATE TRIGGER log_transport_status_change
    BEFORE UPDATE ON transports
    FOR EACH ROW
BEGIN
    IF OLD.status <> NEW.status THEN
    INSERT INTO transport_status (transport_id, status_change, old_status, new_status)
    VALUES (OLD.transport_id, NOW(), OLD.status, NEW.status);
END IF;
END;
