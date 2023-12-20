insert into users(email, username, password, password_last_changed, account_non_locked)
values ('fist@ulstu.ru', 'fist', '$2a$10$LbaXjG5uDaIx2MjRSQibCOSCBTHrwNaPAIXY92sEt2bGyD9nGr53q', now(), true),
       ('rtf@ulstu.ru', 'rtf', '$2a$10$iVNMKPoRv43HTJp69UrAy.tzsG6xyh48FRrbpaHjkSk4lY2iHm8DO', now(), true),
       ('iatu@ulstu.ru', 'iatu', '$2a$10$uBvq5IDzlW.bd4Cyb2niZOm4oGKpT71/4fXCUjkjVNZ0CikoohDyu', now(), true);

insert into user_roles(user_id, roles)
values (1, 'ADMIN'),
       (2, 'ADMIN'),
       (3, 'ADMIN');