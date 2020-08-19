insert into usr (id, username, password, active, file_name, is_physical, person_id)
    values (0, 'admin@admin.admin', '123', true, 'defaultavatar.png', true, 0);

insert into user_role (user_id, roles)
    values (0, 'USER'), (0, 'ADMIN');

insert into human (id, email, first_name, second_name, last_name, user_id)
    values (0, 'admin@admin.admin', 'Админ', 'Админович', 'Админов', 0);