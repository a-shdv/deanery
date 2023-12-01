create table users
(
    id       bigserial primary key not null,
    email    varchar(255),
    username varchar(255),
    password varchar(255)
);