CREATE SCHEMA IF NOT EXISTS vehicle_dashboard;

CREATE USER readonly_user WITH ENCRYPTED PASSWORD 'readonly123';
GRANT CONNECT ON DATABASE vehicle_dashboard TO readonly_user;
GRANT USAGE ON SCHEMA vehicle_dashboard TO readonly_user;
GRANT SELECT ON ALL TABLES IN SCHEMA vehicle_dashboard TO readonly_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA vehicle_dashboard
GRANT SELECT ON TABLES TO readonly_user;