create table chat_users (
    chat_id int8 not null,
    user_id int8 not null,
    primary key (chat_id, user_id)
);

create table organisation_organisation_requests (
    organisation_id int8 not null,
    organisation_requests_id int8 not null
);

create table organisation_requests_humans (
    employee_id int8 not null,
    request_id int8 not null,
    primary key (employee_id, request_id)
);
