--liquibase formatted sql
--changeset ${AUTHOR_NAME}:1.10_auto_payment_date runOnChange:true splitStatements:false stripComments:false endDelimiter:;

CREATE TRIGGER set_payment_date_on_paid
    BEFORE UPDATE ON payments
    FOR EACH ROW
BEGIN
    IF NEW.status = 'paid'
     AND (OLD.status <> 'paid' OR OLD.payment_datetime IS NULL) THEN
    SET NEW.payment_datetime = CURRENT_TIMESTAMP;
END IF;
END;
