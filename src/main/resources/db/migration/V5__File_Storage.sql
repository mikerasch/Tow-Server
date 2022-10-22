/*
 Creates a very basic file storage table that will overwrite the previous image when a new one is entered.
 */

create table public.file_db
(
    id    uuid DEFAULT gen_random_uuid() NOT NULL,
    user_uuid uuid NOT NULL,
    type VARCHAR(255),
    filename VARCHAR(20),
    data bytea,
    CONSTRAINT id_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;
