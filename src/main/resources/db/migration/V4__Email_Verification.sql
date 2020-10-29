

create table ConfirmationToken
(
    confirmationToken varchar(255),
    tokenId SERIAL NOT NULL ,
    CreatedDate date,
    userId  bigint NOT NULL,
PRIMARY KEY (tokenId)
)
  TABLESPACE pg_default;
