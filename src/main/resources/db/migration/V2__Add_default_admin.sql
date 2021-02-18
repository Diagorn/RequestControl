insert into usr (id, username, password, active, file_name, is_physical, person_id)
    values (0, 'admin@admin.admin', '123', true, 'defaultavatar.png', true, 0);

insert into user_role (user_id, roles)
    values (0, 'USER'), (0, 'ADMIN');

insert into human (id, email, first_name, second_name, last_name, user_id, education, passport, phone_number,
                   position, registration_adress, passport_giving_organ, passport_date, citizenship, group_name, index, sex, speciality)
    values (0, 'admin@admin.admin', 'Админ', 'Админович', 'Админов', 0, 'university-doctor', '0000000000', '+7-800-555-35-35', 'Директор',
            'ул. 5-я Парковая д. 10 кв. 154', 'МВД РФ', '1999-12-20 15:51:23.885', 'Россия', '-', '-', false, 'Бизнес-информатика');