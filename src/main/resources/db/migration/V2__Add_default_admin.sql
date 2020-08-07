insert into usr (id, username, password, active, first_name, second_name, last_name, file_name)
    values (0, 'admin@admin.admin', '123', true, 'admin', 'admin', 'admin', 'defaultavatar.png');

insert into user_role (user_id, roles)
    values (0, 'USER'), (0, 'ADMIN');