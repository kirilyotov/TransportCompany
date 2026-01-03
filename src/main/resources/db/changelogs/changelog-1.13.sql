--liquibase formatted sql

--changeset ${AUTHOR_NAME}:1.13_seed_demo_data context:dev runOnChange:true --tag dev

-- Companies
INSERT INTO companies (company_id, name, address, phone, created_at, updated_at)
VALUES (1, 'North Logistics', '101 Northern Ave, City A', '+3591111111', '2025-01-01 08:00:00', '2025-01-01 08:00:00')
ON DUPLICATE KEY UPDATE name = VALUES(name), updated_at = VALUES(updated_at);

INSERT INTO companies (company_id, name, address, phone, created_at, updated_at)
VALUES (2, 'South Transit', '202 Southern Blvd, City B', '+3592222222', '2025-01-02 08:00:00', '2025-01-02 08:00:00')
ON DUPLICATE KEY UPDATE name = VALUES(name), updated_at = VALUES(updated_at);

-- Clients
INSERT INTO clients (client_id, name, surname, phone, email, address, company_id, created_at, updated_at)
VALUES
	(1, 'Alice', 'Ivanova', '+3598881001', 'alice@example.com', 'A Street 1', 1, '2025-01-03 09:00:00', '2025-01-03 09:00:00'),
	(2, 'Bob', 'Petrov', '+3598881002', 'bob@example.com', 'B Street 2', 1, '2025-01-03 09:05:00', '2025-01-03 09:05:00'),
	(3, 'Carol', 'Dimitrova', '+3598882001', 'carol@example.com', 'C Street 3', 2, '2025-01-04 09:10:00', '2025-01-04 09:10:00'),
	(4, 'Dave', 'Georgiev', '+3598882002', 'dave@example.com', 'D Street 4', 2, '2025-01-04 09:15:00', '2025-01-04 09:15:00')
ON DUPLICATE KEY UPDATE name = VALUES(name), surname = VALUES(surname), updated_at = VALUES(updated_at);

-- Employees (drivers)
INSERT INTO employees (employee_id, name, ucn, phone, salary, qualifications, company_id, created_at, updated_at)
VALUES
	(1, 'Driver One', '9000000001', '+3597001001', 2200.00, JSON_ARRAY('hazardous', 'tanker'), 1, '2025-01-05 10:00:00', '2025-01-05 10:00:00'),
	(2, 'Driver Two', '9000000002', '+3597001002', 1800.00, JSON_ARRAY('bus', 'over_12_passengers'), 1, '2025-01-05 10:05:00', '2025-01-05 10:05:00'),
	(3, 'Driver Three', '9000000003', '+3597002003', 2000.00, JSON_ARRAY('truck'), 2, '2025-01-06 10:10:00', '2025-01-06 10:10:00'),
	(4, 'Driver Four', '9000000004', '+3597002004', 2100.00, JSON_ARRAY('bus', 'over_12_passengers'), 2, '2025-01-06 10:15:00', '2025-01-06 10:15:00')
ON DUPLICATE KEY UPDATE name = VALUES(name), salary = VALUES(salary), updated_at = VALUES(updated_at);

-- Vehicles
INSERT INTO vehicles (vehicle_id, license_plate, type, capacity_weight, capacity_passengers, company_id, status, created_at, updated_at)
VALUES
	(1, 'ABC-1001', 'tanker', 15000.00, NULL, 1, 'active', '2025-01-07 11:00:00', '2025-01-07 11:00:00'),
	(2, 'BUS-2001', 'bus', NULL, 50, 1, 'active', '2025-01-07 11:05:00', '2025-01-07 11:05:00'),
	(3, 'TRK-3001', 'truck', 12000.00, NULL, 2, 'in_repair', '2025-01-08 11:10:00', '2025-01-08 11:10:00'),
	(4, 'BUS-4001', 'bus', NULL, 40, 2, 'active', '2025-01-08 11:15:00', '2025-01-08 11:15:00')
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = VALUES(updated_at);

-- Transports
INSERT INTO transports (transport_id, start_point, end_point, departure_date, arrival_date, vehicle_id, driver_id, client_id, price, company_id, status, created_at, updated_at)
VALUES
	(1, 'Sofia', 'Varna', '2025-01-10 08:00:00', '2025-01-10 14:00:00', 1, 1, 1, 3500.00, 1, 'completed', '2025-01-09 12:00:00', '2025-01-10 14:00:00'),
	(2, 'Plovdiv', 'Burgas', '2025-01-15 09:00:00', '2025-01-15 13:30:00', 2, 2, 2, 1200.00, 1, 'completed', '2025-01-14 12:00:00', '2025-01-15 13:30:00'),
	(3, 'Sofia', 'Ruse', '2025-01-18 07:30:00', NULL, 3, 3, 3, 2800.00, 2, 'in_progress', '2025-01-17 12:00:00', '2025-01-18 07:30:00'),
	(4, 'Varna', 'Sofia', '2025-01-20 16:00:00', '2025-01-21 01:00:00', 4, 4, 4, 1500.00, 2, 'planned', '2025-01-19 12:00:00', '2025-01-19 12:00:00')
ON DUPLICATE KEY UPDATE status = VALUES(status), arrival_date = VALUES(arrival_date), updated_at = VALUES(updated_at);

-- Cargo
INSERT INTO cargo (cargo_id, transport_id, type, description, total_weight, passenger_count, created_at, updated_at)
VALUES
	(1, 1, 'goods', 'Fuel delivery', 14000.00, 0, '2025-01-10 08:15:00', '2025-01-10 08:15:00'),
	(2, 2, 'passengers', 'Intercity passengers', NULL, 45, '2025-01-15 09:15:00', '2025-01-15 09:15:00'),
	(3, 3, 'goods', 'Construction materials', 11000.00, 0, '2025-01-18 07:45:00', '2025-01-18 07:45:00'),
	(4, 4, 'passengers', 'Team transfer', NULL, 35, '2025-01-20 16:15:00', '2025-01-20 16:15:00')
ON DUPLICATE KEY UPDATE description = VALUES(description), updated_at = VALUES(updated_at);

-- Payments
INSERT INTO payments (payment_id, transport_id, client_id, amount, payment_datetime, status, created_at, updated_at)
VALUES
	(1, 1, 1, 3500.00, '2025-01-09 10:00:00', 'paid', '2025-01-09 09:30:00', '2025-01-09 10:00:00'),
	(2, 2, 2, 1200.00, '2025-01-16 12:00:00', 'partial', '2025-01-15 12:30:00', '2025-01-16 12:00:00'),
	(3, 3, 3, 2800.00, NULL, 'unpaid', '2025-01-18 12:30:00', '2025-01-18 12:30:00'),
	(4, 4, 4, 1500.00, NULL, 'unpaid', '2025-01-20 12:30:00', '2025-01-20 12:30:00')
ON DUPLICATE KEY UPDATE status = VALUES(status), payment_datetime = VALUES(payment_datetime), updated_at = VALUES(updated_at);

-- Transport status history
INSERT INTO transport_status (status_id, transport_id, status_change, old_status, new_status, notes, created_at, updated_at)
VALUES
	(1, 1, '2025-01-09 08:00:00', 'planned', 'in_progress', 'Departed on time', '2025-01-09 08:00:00', '2025-01-09 08:00:00'),
	(2, 1, '2025-01-10 14:00:00', 'in_progress', 'completed', 'Arrived at Varna', '2025-01-10 14:00:00', '2025-01-10 14:00:00'),
	(3, 2, '2025-01-15 09:00:00', 'planned', 'in_progress', 'Loaded passengers', '2025-01-15 09:00:00', '2025-01-15 09:00:00'),
	(4, 2, '2025-01-15 13:30:00', 'in_progress', 'completed', 'Trip finished', '2025-01-15 13:30:00', '2025-01-15 13:30:00'),
	(5, 3, '2025-01-18 07:30:00', 'planned', 'in_progress', 'Departed early', '2025-01-18 07:30:00', '2025-01-18 07:30:00'),
	(6, 4, '2025-01-19 12:00:00', 'planned', 'planned', 'Pre-trip check complete', '2025-01-19 12:00:00', '2025-01-19 12:00:00')
ON DUPLICATE KEY UPDATE new_status = VALUES(new_status), notes = VALUES(notes), updated_at = VALUES(updated_at);
