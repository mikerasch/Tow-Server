create table public.driver
(
    longitude  float4  NOT NULL default 0.0,
    latitude   float4  NOT NULL default 0.0,
    active     boolean NOT NULL default false
) inherits (public.users);

create table public.dispatcher
(
    precinct varchar(255)
) inherits (public.users);
