/*
 Creates user tables that will be used to create later role tables
 that will inherit these attributes. if its not happy make sure that youve connected the
 files to a datasource. This may not be done even if the config and flyaway are as they should be.
 */

 CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE public.users
(
    id           uuid DEFAULT gen_random_uuid() NOT NULL,
    email        character varying(100) COLLATE pg_catalog."default",
    firstname    character varying(20) COLLATE pg_catalog."default",
    lastname     character varying(20) COLLATE pg_catalog."default",
    password     character varying(120) COLLATE pg_catalog."default",
    role         character varying(255) COLLATE pg_catalog."default",
    username     character varying(100) COLLATE pg_catalog."default",
    phone        character varying(20) COLLATE pg_catalog."default",
    reset_date   character varying(750),
    verify_date  character varying(750),
    verify_token character varying(750),
    ver_enabled  boolean,
    reset_token  int,
    fb_token     character varying(750),
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username)
) WITH (OIDS = FALSE)
  TABLESPACE pg_default;

create index users_id on users (id);
create index users_email on users (email);
create index users_username on users (username);

SET TIMEZONE = 'America/Chicago';
