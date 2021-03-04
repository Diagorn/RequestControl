insert into usr (id, username, password, active, file_name, is_physical)
values (1, 'KlimovaMarA@mpei.ru', 'qweqwe', true, 'defaultavatar.png', true);

insert into human (id, email, first_name, second_name, last_name, user_id, phone_number, sex)
values (1, 'KlimovaMarA@mpei.ru', 'Марина', 'Александровна', 'Климова', 1, '+7 495 362-75-92', false);

update usr set person_id = 1 where id = 1;

insert into user_role (user_id, roles)
values (1, 'USER'), (1, 'ADMIN'), (1, 'EXECUTER'), (1, 'MODER');

insert into usr (id, username, password, active, file_name, is_physical)
values (2, 'BologovPI@mpei.ru', 'qweqwe', true, 'defaultavatar.png', true);

insert into human (id, email, first_name, second_name, last_name, user_id, phone_number, sex)
values (2, 'BologovPI@mpei.ru', 'Бологов', 'Пётр', 'Иванович', 2, '+7 495 362-73-05', true);

update usr set person_id = 2 where id = 2;

insert into user_role (user_id, roles)
values (2, 'USER'), (2, 'ADMIN'), (2, 'EXECUTER'), (2, 'MODER');

