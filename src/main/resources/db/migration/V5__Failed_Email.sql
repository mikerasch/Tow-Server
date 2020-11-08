create table failed_email
(
    uuid         uuid        NOT NULL,
    email        varchar(50) NOT NULL,
    user_uuid    varchar(36) NOT NULL,
    firstname    varchar(20) NOT NULL,
    lastname     varchar(20) NOT NULL,
    verify_token varchar(32) NOT NULL,
    retries      int         NOT NULL DEFAULT 0
)
    WITH (OIDS = FALSE)
    TABLESPACE pg_default;

create unique index failed_email_uuid_uindex
    on failed_email (uuid);