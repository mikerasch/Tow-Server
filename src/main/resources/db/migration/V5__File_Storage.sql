/*
 Creates a very basic file storage table that will overwrite the previous image when a new one is entered.
 */

create table public.file_db
(
    id    uuid DEFAULT gen_random_uuid() NOT NULL,
    user_uuid uuid NOT NULL,
    type VARCHAR(255),
    filename VARCHAR(1024),
    data bytea,
    location VARCHAR(255),
    date  timestamp NOT NULL,
    CONSTRAINT id_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;
