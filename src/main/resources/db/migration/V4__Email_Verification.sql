

alter table users
    add reset_date varchar(10);

alter table users
    add verify_token varchar(20);

create unique index users_verify_token_uindex
    on users (verify_token);

alter table users
    add verify_date varchar(10);

-- fix to be not null later
alter table users
    add ver_enabled boolean;