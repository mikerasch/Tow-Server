create table sp_admin
(
    id          uuid DEFAULT gen_random_uuid() NOT NULL,
    firstname   varchar(50),
    lastname    varchar(50),
    username     character varying(100),
    email       varchar(75),
    password    varchar(150),
    phone       varchar(50),
    role        varchar(25),
    reset_date   character varying(750),
    verify_date  character varying(750),
    verify_token character varying(750),
    ver_enabled  boolean,
    reset_token  int,

    CONSTRAINT spid_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;