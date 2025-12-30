--liquibase formatted sql
--changeset ${AUTHOR_NAME}:1.9_validate_transport_assignment runOnChange:true splitStatements:false stripComments:false endDelimiter:;

CREATE TRIGGER validate_transport_assignment
    BEFORE INSERT ON transports
    FOR EACH ROW
BEGIN
    DECLARE driver_quals JSON;
  DECLARE vehicle_type VARCHAR(20);
  DECLARE has_required_qual BOOLEAN DEFAULT TRUE;

    SELECT qualifications
    INTO driver_quals
    FROM employees
    WHERE employee_id = NEW.driver_id;

    SELECT type
    INTO vehicle_type
    FROM vehicles
    WHERE vehicle_id = NEW.vehicle_id;

    IF vehicle_type = 'tanker' THEN
    SET has_required_qual = JSON_CONTAINS(driver_quals, JSON_QUOTE('hazardous'));
  ELSEIF vehicle_type = 'bus' THEN
    SET has_required_qual = JSON_CONTAINS(driver_quals, JSON_QUOTE('over_12_passengers'));
END IF;

IF has_required_qual = 0 THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Driver lacks required qualifications for vehicle';
END IF;
END;
