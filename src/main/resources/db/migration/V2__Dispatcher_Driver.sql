/*
 Creates each subordinate table inheriting off of the main users table
 */

create table public.pd_admin
(
    city             character varying(100),
    address_number   int NOT NULL default 0.0,
    department       character varying(100),
    department_short character varying(100)
) inherits (public.users);


create table public.pd_user
(
    front_id   character varying(100),
    admin_UUID uuid
) inherits (public.users);


create table public.tc_admin
(
    company character varying(255)
) inherits (public.users);


create table public.tc_user
(
    longitude  float4  NOT NULL default 0.0,
    latitude   float4  NOT NULL default 0.0,
    active     boolean NOT NULL default false,
    admin_UUID uuid
) inherits (public.users);

create table public.drivers
(

) inherits (public.tc_user);