DELETE
FROM meals;
DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (description, date_time, calories, user_id)
VALUES ('Завтрак', '2020-01-30 10:00', 500, 100000),
       ('Обед', '2020-01-30 13:00', 1000, 100000),
       ('Ужин', '2020-01-30 20:00', 500, 100000),
       ('Еда на граничное значение', '2020-01-31 00:00', 100, 100000),
       ('Завтрак', '2020-01-31 10:00', 1000, 100000),
       ('Обед', '2020-01-31 13:00', 500, 100000),
       ('Ужин', '2020-01-31 20:00', 410, 100000),
       ('Завтрак', '2020-02-01 08:00', 1000, 100000),
       ('Админ ланч', '2015-06-01 14:00', 510, 100001),
       ('Админ ужин', '2015-06-01 21:00', 1500, 100001);
