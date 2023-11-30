create table users_groups
(
    user_id   bigint not null,
    groups_id bigint not null unique
);