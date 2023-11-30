create table timetables
(
    id         bigserial primary key not null,
    subject_id bigint,
    teacher_id bigint
);