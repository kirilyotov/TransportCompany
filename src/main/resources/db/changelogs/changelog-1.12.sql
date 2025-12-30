--liquibase formatted sql
--changeset ${AUTHOR_NAME}:1.12_get_company_revenue_period_calendar runOnChange:true splitStatements:false stripComments:false endDelimiter:;
CREATE FUNCTION get_company_revenue_period(
    p_company_id INT,
    p_period_type ENUM('day', 'week', 'month', 'quarter', 'year'),
    p_period_offset INT
        ) RETURNS DECIMAL(12,2)
    READS SQL DATA
    DETERMINISTIC
BEGIN
    DECLARE v_start DATETIME;
    DECLARE v_end   DATETIME;
    DECLARE v_base  DATE;

    SET v_base = CURDATE();

CASE p_period_type
        WHEN 'day' THEN
            SET v_start = TIMESTAMP(DATE_SUB(v_base, INTERVAL p_period_offset DAY), '00:00:00');
            SET v_end   = DATE_ADD(v_start, INTERVAL 1 DAY);

WHEN 'week' THEN
            SET v_start = TIMESTAMP(
                DATE_SUB(DATE_SUB(v_base, INTERVAL p_period_offset WEEK), INTERVAL WEEKDAY(DATE_SUB(v_base, INTERVAL p_period_offset WEEK)) DAY),
                '00:00:00'
            );
            SET v_end = DATE_ADD(v_start, INTERVAL 7 DAY);

WHEN 'month' THEN
            SET v_start = TIMESTAMP(
                STR_TO_DATE(DATE_FORMAT(DATE_SUB(v_base, INTERVAL p_period_offset MONTH), '%Y-%m-01'), '%Y-%m-%d'),
                '00:00:00'
            );
            SET v_end = DATE_ADD(v_start, INTERVAL 1 MONTH);

WHEN 'quarter' THEN
            SET v_start = TIMESTAMP(
                STR_TO_DATE(
                    CONCAT(
                        YEAR(DATE_SUB(v_base, INTERVAL p_period_offset QUARTER)), '-',
                        LPAD(((QUARTER(DATE_SUB(v_base, INTERVAL p_period_offset QUARTER)) - 1) * 3 + 1), 2, '0'),
                        '-01'
                    ),
                    '%Y-%m-%d'
                ),
                '00:00:00'
            );
            SET v_end = DATE_ADD(v_start, INTERVAL 3 MONTH);

WHEN 'year' THEN
            SET v_start = TIMESTAMP(
                STR_TO_DATE(DATE_FORMAT(DATE_SUB(v_base, INTERVAL p_period_offset YEAR), '%Y-01-01'), '%Y-%m-%d'),
                '00:00:00'
            );
            SET v_end = DATE_ADD(v_start, INTERVAL 1 YEAR);
END CASE;

RETURN get_company_revenue(p_company_id, v_start, v_end);
END;
