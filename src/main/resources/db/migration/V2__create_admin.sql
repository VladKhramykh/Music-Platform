insert into users (created_by, created_date, deleted, last_modified_by, last_modified_date, version, activation_code,
                   birthday, country,
                   email, first_name, gender, hash_password, last_name, photo_uri)
values ('Administrator', '2020-12-17 16:51:48.0260000', 0, 'Administrator', '2020-12-17 16:53:05.2350000', 1,
        '20b60c57-76ed-48ea-b400-1a5eb0a4393b', '2020-12-04 00:00:00.0000000', 'JAPAN', 'xramyxv@mail.ru', 'Vlad',
        'MALE', '$2a$10$wXZklM39Y3uuG6x1Qug0w.PrFSAH10rBDUuZo8HlrJAH6gtpQuC4y', 'Khramykh', '');
insert into user_role
values (1, 'ADMIN');
insert into user_role
values (1, 'USER');
