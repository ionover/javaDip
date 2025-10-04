CREATE TABLE IF NOT EXISTS data.users
(
    id                 uuid                   NOT NULL DEFAULT gen_random_uuid(),
    login              character varying      NOT NULL,
    password           character varying      NOT NULL,
    jwtToken           character varying,

    CONSTRAINT users_pkey PRIMARY KEY (id)
    ) TABLESPACE pg_default;

INSERT INTO data.users
(login,password)
VALUES ('admin', 'admin');

INSERT INTO data.users
(login,password)
VALUES ('user', 'user');
