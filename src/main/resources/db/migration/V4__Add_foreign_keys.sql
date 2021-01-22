alter table if exists organisation_organisation_requests
    add constraint organisations_requests
    unique (organisation_requests_id);

alter table if exists chat
    add constraint chat_organisation foreign key (organisation_request_id)
    references organisation_request;

alter table if exists chat
    add constraint chat_physical_request_FK foreign key (physical_request_id)
    references physical_request;

alter table if exists chat_users
    add constraint chat_users_FK foreign key (user_id)
    references usr;

alter table if exists chat_users
    add constraint chat_users_chat_FK foreign key (chat_id)
    references chat;

alter table if exists human
    add constraint human_user_FK foreign key (user_id)
    references usr;

alter table if exists human
    add constraint human_organisation_request_FK foreign key (organisation_request_id)
        references organisation_request;

alter table if exists message
    add constraint message_user_FK foreign key (user_id)
    references usr;

alter table if exists message
    add constraint chat_message_FK foreign key (chat_id)
    references chat;

alter table if exists organisation
    add constraint organisation_director foreign key (director_id)
    references human;

alter table if exists organisation
    add constraint organisation_user_FK foreign key (user_id)
    references usr;

alter table if exists organisation_organisation_requests
    add constraint organisation_requests_requests_FK foreign key (organisation_requests_id)
    references organisation_request;

alter table if exists organisation_organisation_requests
    add constraint organisation_requests_organisation_FK foreign key (organisation_id)
    references organisation;

alter table if exists organisation_requests_humans
    add constraint organisation_requests_humans_FK foreign key (request_id)
    references organisation_request;

alter table if exists organisation_requests_humans
    add constraint organisation_requests_employee_FK foreign key (employee_id)
    references human;

alter table if exists organisation_request
    add constraint organisation_request_chat_FK foreign key (chat_id)
    references chat;

alter table if exists organisation_request
    add constraint organisation_request_client_FK foreign key (client_id)
    references usr;

alter table if exists organisation_request
    add constraint organisation_request_executer_FK foreign key (executer_id)
    references usr;

alter table if exists organisation_request
    add constraint organisation_request_organisation_FK foreign key (organisation_id)
    references organisation;

alter table if exists physical_request
    add constraint physical_request_chat_FK foreign key (chat_id)
    references chat;

alter table if exists physical_request
    add constraint physical_request_client_FK foreign key (client_id)
    references usr;

alter table if exists physical_request
    add constraint physical_request_executer_FK foreign key (executer_id)
    references usr;

alter table if exists user_role
    add constraint user_role_user_FK foreign key (user_id)
    references usr;

alter table if exists usr
    add constraint user_organisation_FK foreign key (organisation_id)
    references organisation;

alter table if exists usr
    add constraint user_human_FK foreign key (person_id)
    references human;