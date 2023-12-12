create table teachers
(
    id   bigserial primary key not null,
    first_name varchar(255),
    last_name varchar(255),
    patronymic_name varchar(255)
);