create table groups
(
    user_id bigint,
    id      bigserial not null,
    title   varchar(255),
    primary key (id)
);