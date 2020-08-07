insert into chat (id)
    values (0);

insert into chat_users (chat_id, user_id)
    select 0, user_id from user_role
        where roles like '%ADMIN%' or roles like '%MODER%';