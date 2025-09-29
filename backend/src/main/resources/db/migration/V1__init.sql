CREATE TABLE IF NOT EXISTS data.files
(
    id                 uuid                   NOT NULL,
    title              character varying      NOT NULL,
    size               bigint,
    extension          character varying(10),
    path               character varying(500) NOT NULL,
    created_by         character varying(50),
    created_at         timestamp without time zone,
    CONSTRAINT files_pkey PRIMARY KEY (id)
    ) TABLESPACE pg_default;

-- ALTER TABLE data.files
--     OWNER to ${db_owner};