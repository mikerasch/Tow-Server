CREATE TABLE public.users
(
    uuid        character varying(36) COLLATE pg_catalog."default",
    email       character varying(100) COLLATE pg_catalog."default",
    firstname   character varying(20) COLLATE pg_catalog."default",
    lastname    character varying(20) COLLATE pg_catalog."default",
    password    character varying(120) COLLATE pg_catalog."default",
    role        character varying(255) COLLATE pg_catalog."default",
    username    character varying(100) COLLATE pg_catalog."default",
    reset_token character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (uuid),
    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username)
)
WITH (
    OIDS = FALSE
)

TABLESPACE pg_default;

CREATE SEQUENCE roles_id_seq;

CREATE TABLE public.roles
(
    id integer NOT NULL DEFAULT nextval('roles_id_seq'::regclass),
    name character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)

TABLESPACE pg_default;

INSERT INTO roles(name) VALUES('ROLE_ADMIN');
INSERT INTO roles(name) VALUES('ROLE_DRIVER');
INSERT INTO roles(name) VALUES('ROLE_DISPATCHER');

SET TIMEZONE='America/Chicago';