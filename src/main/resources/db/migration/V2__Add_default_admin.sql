insert into usr (id, username, password, active, file_name, is_physical, person_id)
    values (0, 'admin@admin.admin', 'qweqwe', true, 'defaultavatar.png', true, 0);

insert into user_role (user_id, roles)
    values (0, 'USER'), (0, 'ADMIN');

insert into human (id, email, first_name, second_name, last_name, user_id, phone_number, sex)
    values (0, 'admin@admin.admin', 'Админ', 'Админович', 'Админов', 0, '+7-800-555-35-35', false);