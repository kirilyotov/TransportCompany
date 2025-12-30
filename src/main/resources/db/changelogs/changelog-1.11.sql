--liquibase formatted sql
--changeset ${AUTHOR_NAME}:1.11_get_company_revenue runOnChange:true splitStatements:false stripComments:false endDelimiter:;
CREATE FUNCTION get_company_revenue(
    p_company_id INT,
    p_start_datetime DATETIME,
    p_end_datetime DATETIME
) RETURNS DECIMAL(12,2)
    READS SQL DATA
    DETERMINISTIC
BEGIN
    DECLARE v_revenue DECIMAL(12,2) DEFAULT 0;

SELECT COALESCE(SUM(p.amount), 0) INTO v_revenue
FROM payments p
         JOIN transports t ON p.transport_id = t.transport_id
WHERE t.company_id = p_company_id
  AND p.status = 'paid'
  AND p.payment_datetime BETWEEN p_start_datetime AND p_end_datetime;

RETURN v_revenue;
END;
