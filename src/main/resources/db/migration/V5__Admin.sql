create table public.pd_admin
(
    precinct character varying(255)
) inherits (public.users);

create table public.tc_admin
(
    company varchar(255)
) inherits (public.users);
