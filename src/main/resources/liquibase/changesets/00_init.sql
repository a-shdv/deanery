create table classrooms
(
    classroom_no integer unique,
    id           bigserial not null,
    primary key (id)
);
create table groups
(
    id    bigserial not null,
    title varchar(255),
    primary key (id)
);
create table roles
(
    id    bigserial not null,
    title varchar(255),
    primary key (id)
);
create table subjects
(
    id    bigserial not null,
    title varchar(255),
    primary key (id)
);
create table teachers
(
    id              bigserial not null,
    first_name      varchar(255),
    last_name       varchar(255),
    patronymic_name varchar(255),
    primary key (id)
);
create table teachers_subjects
(
    id         bigserial not null,
    subject_id bigint,
    teacher_id bigint,
    primary key (id)
);
create table timetables
(
    day_of_week   smallint check (day_of_week between 0 and 6),
    time_of_class smallint check (time_of_class between 0 and 8),
    classroom_id  bigint,
    group_id      bigint,
    id            bigserial not null,
    subject_id    bigint,
    teacher_id    bigint,
    primary key (id)
);
create table users
(
    account_non_locked    boolean   not null,
    id                    bigserial not null,
    password_last_changed timestamp(6),
    email                 varchar(255),
    password              varchar(255),
    username              varchar(255),
    primary key (id)
);
create table users_roles
(
    role_id bigint not null,
    user_id bigint not null
);

alter table if exists teachers_subjects add constraint FK60390cxlor02x2h4wra2gge2b foreign key (subject_id) references subjects;
alter table if exists teachers_subjects add constraint FKncdyxoidjk44jwkexcltr87ay foreign key (teacher_id) references teachers;
alter table if exists timetables add constraint FK18lp6tw732sd0od8jh4aytdn8 foreign key (classroom_id) references classrooms;
alter table if exists timetables add constraint FKgiq0e7ecg94hdk9on7eslyn08 foreign key (group_id) references groups;
alter table if exists timetables add constraint FKgud9rcaxdpwxad8k1ubijyfem foreign key (subject_id) references subjects;
alter table if exists timetables add constraint FKhw9tm03jm0du6i411hc0gftu9 foreign key (teacher_id) references teachers;
alter table if exists users_roles add constraint FKj6m8fwv7oqv74fcehir1a9ffy foreign key (role_id) references roles;
alter table if exists users_roles add constraint FK2o0jvgh89lemvvo17cbqvdxaa foreign key (user_id) references users;
