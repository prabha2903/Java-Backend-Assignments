-- ============================================================
-- Sample data for the Student Management System
-- ------------------------------------------------------------
-- The application creates the `students` table automatically
-- (spring.jpa.hibernate.ddl-auto=update) the first time it runs,
-- so simply START THE APPLICATION ONCE before running this script.
--
-- Usage (MySQL client / Workbench):
--   mysql -u root -p studentdb < sample-data.sql
-- ============================================================

USE studentdb;

INSERT INTO students (name, email, phone_number, department, year_of_study, cgpa) VALUES
('Arun Kumar',        'arun.kumar@example.com',        '9876543210', 'Computer Science',        2, 8.75),
('Divya Sri',         'divya.sri@example.com',         '9876543211', 'Computer Science',        3, 9.10),
('Karthik Raja',      'karthik.raja@example.com',      '9876543212', 'Electronics',             1, 7.40),
('Priya Dharshini',   'priya.dharshini@example.com',   '9876543213', 'Mechanical',              4, 6.85),
('Sanjay Varma',      'sanjay.varma@example.com',      '9876543214', 'Information Technology',  2, 8.20),
('Meena Loshini',     'meena.loshini@example.com',     '9876543215', 'Electronics',             3, 9.45),
('Ravi Shankar',      'ravi.shankar@example.com',      '9876543216', 'Civil Engineering',       1, 5.90),
('Anitha Ramesh',     'anitha.ramesh@example.com',     '9876543217', 'Computer Science',        4, 9.60);
