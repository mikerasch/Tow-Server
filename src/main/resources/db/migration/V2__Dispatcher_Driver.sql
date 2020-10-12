create table driver
(
    user_uuid          varchar
        constraint fkq1vwu6wnagupnprl74s7qs6gu
            references users,
    business           varchar(255),
    cdl_licence_number varchar(255)
) WITH ( OIDS = FALSE ) TABLESPACE pg_default;

create unique index driver_user_uuid_uindex
    on driver (user_uuid);

create table dispatcher
(
    user_uuid varchar
        constraint fkq9daf4lrtvmspl72pns23qtp4
            references users,
    precinct  varchar(255)
) WITH ( OIDS = FALSE ) TABLESPACE pg_default;

create unique index dispatcher_user_uuid_uindex
    on dispatcher (user_uuid);
