/*
 creates a table for the un verified emails that are enter in order
 to perform chron jobs and eventually time the accounts out(delete them).
 */
create table failed_email
(
    uuid         uuid        NOT NULL,
    user_uuid    uuid        NOT NULL,
    email        varchar(50) NOT NULL,
    firstname    varchar(20) NOT NULL,
    lastname     varchar(20) NOT NULL,
    verify_token varchar(32) NOT NULL,
    retries      int         NOT NULL DEFAULT 0
)
    WITH (OIDS = FALSE)
    TABLESPACE pg_default;

create unique index failed_email_uuid_uindex
    on failed_email (uuid);