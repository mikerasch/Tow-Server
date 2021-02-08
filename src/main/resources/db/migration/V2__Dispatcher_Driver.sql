create table public.pdadmin
(
    precinct character varying(255)
) inherits (public.users);

create table public.tcadmin
(
    company varchar(255)
) inherits (public.users);
