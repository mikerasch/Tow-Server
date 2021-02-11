
create table public.file_db
(
    id       uuid                     DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    data     bytea

--     name     character varying(100),
--     type     character varying(100),
--     useruuid uuid,

--     created  timestamp with time zone default current_timestamp  not null
);