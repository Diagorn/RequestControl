create table if not exists chat_users (
    chat_id int8 not null references chat,
    user_id int8 not null references usr,
    primary key (chat_id, user_id)
);