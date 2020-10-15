create table driver
(
    uuid character varying(36)
        constraint fkq1vwu6wnagupnprl74s7qs6gu
            references users,
    longitude float4 NOT NULL,
    latitude  float4 NOT NULL,
    active  boolean NOT NULL
) WITH ( OIDS = FALSE )
  TABLESPACE pg_default;

create unique index driver_uuid_uindex
    on driver (uuid);

create table dispatcher
(
    uuid character varying(36)
        constraint fkq9daf4lrtvmspl72pns23qtp4
            references users,
    precinct  varchar(255)
) WITH ( OIDS = FALSE )
  TABLESPACE pg_default;

create unique index dispatcher_uuid_uindex
    on dispatcher (uuid);
