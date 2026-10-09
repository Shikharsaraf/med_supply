-- 1. Insert Facilities
INSERT INTO facilities (name, facility_type, district, latitude, longitude, capacity, created_at)
VALUES 
('Central Medical Warehouse', 'REGIONAL_WAREHOUSE', 'Central District', 28.6139, 77.2090, 50000, NOW()),
('District General Hospital', 'DISTRICT_HOSPITAL', 'North District', 28.7041, 77.1025, 12000, NOW()),
('Community Health Center 4', 'PRIMARY_CLINIC', 'East District', 28.6280, 77.2789, 3000, NOW());

-- 2. Insert Medicines
INSERT INTO medicines (name, category, unit, criticality, shelf_life_days, storage_requirements, created_at)
VALUES
('Amoxicillin 500mg', 'Antibiotic', 'Capsules', 'HIGH', 730, 'Keep dry below 25C', NOW()),
('Insulin Glargine 100IU/ml', 'Endocrine', 'Vials', 'CRITICAL', 365, 'Refrigerated 2C - 8C', NOW()),
('Paracetamol 500mg', 'Analgesic', 'Tablets', 'LOW', 1095, 'Room temperature', NOW()),
('Rabies Vaccine', 'Immunological', 'Doses', 'CRITICAL', 540, 'Cold chain 2C - 8C', NOW());

-- 3. Insert Inventories
-- Central Warehouse (Facility 1): Healthy / Surplus inventory
INSERT INTO inventories (facility_id, medicine_id, quantity, minimum_level, maximum_level, expiry_date, updated_at)
VALUES
(1, 1, 15000, 2000, 20000, CURRENT_DATE + INTERVAL '300 days', NOW()),
(1, 2, 4000, 500, 6000, CURRENT_DATE + INTERVAL '200 days', NOW());

-- District Hospital (Facility 2): Moderate inventory
INSERT INTO inventories (facility_id, medicine_id, quantity, minimum_level, maximum_level, expiry_date, updated_at)
VALUES
(2, 1, 1200, 400, 3000, CURRENT_DATE + INTERVAL '240 days', NOW()),
(2, 2, 180, 150, 1000, CURRENT_DATE + INTERVAL '120 days', NOW());

-- Primary Clinic (Facility 3): Low stock
INSERT INTO inventories (facility_id, medicine_id, quantity, minimum_level, maximum_level, expiry_date, updated_at)
VALUES
(3, 1, 110, 200, 800, CURRENT_DATE + INTERVAL '180 days', NOW()),
(3, 2, 25, 60, 250, CURRENT_DATE + INTERVAL '90 days', NOW());

-- 4. Insert Consumption Records for Facility 3 (Clinic 3 consumes ~15 units/day)
INSERT INTO consumption_records (facility_id, medicine_id, consumption_date, quantity_consumed)
VALUES
(3, 1, CURRENT_DATE - INTERVAL '1 day', 16),
(3, 1, CURRENT_DATE - INTERVAL '2 days', 14),
(3, 1, CURRENT_DATE - INTERVAL '3 days', 18),
(3, 1, CURRENT_DATE - INTERVAL '4 days', 15),
(3, 1, CURRENT_DATE - INTERVAL '5 days', 12),
(3, 1, CURRENT_DATE - INTERVAL '6 days', 15),
(3, 1, CURRENT_DATE - INTERVAL '7 days', 17),
(3, 1, CURRENT_DATE - INTERVAL '8 days', 14),
(3, 1, CURRENT_DATE - INTERVAL '9 days', 15),
(3, 1, CURRENT_DATE - INTERVAL '10 days', 14),
(3, 2, CURRENT_DATE - INTERVAL '1 day', 9),
(3, 2, CURRENT_DATE - INTERVAL '2 days', 7),
(3, 2, CURRENT_DATE - INTERVAL '3 days', 8),
(3, 2, CURRENT_DATE - INTERVAL '4 days', 10),
(3, 2, CURRENT_DATE - INTERVAL '5 days', 8);