create table driver
(
    user_uuid varchar
        constraint fkq1vwu6wnagupnprl74s7qs6gu
            references users,
    longitude float4 NOT NULL,
    latitude  float4 NOT NULL,
    active  boolean NOT NULL
) WITH ( OIDS = FALSE )
  TABLESPACE pg_default;

create unique index driver_user_uuid_uindex
    on driver (user_uuid);

create table dispatcher
(
    user_uuid varchar
        constraint fkq9daf4lrtvmspl72pns23qtp4
            references users,
    precinct  varchar(255)
) WITH ( OIDS = FALSE )
  TABLESPACE pg_default;

create unique index dispatcher_user_uuid_uindex
    on dispatcher (user_uuid);
