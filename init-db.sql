-- Создание пользователя pgAdmin (если не существует)
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'pgAdmin') THEN
        CREATE ROLE "pgAdmin" WITH LOGIN PASSWORD 'pdPass';
    END IF;
END
$$;

-- Предоставление прав пользователю pgAdmin
ALTER ROLE "pgAdmin" CREATEDB;
GRANT ALL PRIVILEGES ON DATABASE backend_db TO "pgAdmin";

-- Создание схемы data
CREATE SCHEMA IF NOT EXISTS data;

-- Предоставление прав на схему data
GRANT ALL ON SCHEMA data TO "pgAdmin";