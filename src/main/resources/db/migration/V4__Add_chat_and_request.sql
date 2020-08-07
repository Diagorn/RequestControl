create table if not exists chat (
    id int8 not null,
    request_id int8,
    primary key (id)
);

create table if not exists request (
    id int8 not null,
    theme varchar(255),
    chat_id int8,
    client_id int8,
    executer_id int8,
    primary key (id)
);

alter table if exists request
    add constraint request_chat_fk
    foreign key (chat_id) references chat;

alter table if exists request
    add constraint request_client_fk
    foreign key (client_id) references usr;

alter table if exists request
    add constraint request_executer_fk
    foreign key (executer_id) references usr;

alter table if exists chat
    add constraint chat_request_chat_fk
    foreign key (request_id) references request;

alter table if exists message
    add constraint message_chat_message_fk
    foreign key (chat_id) references chat