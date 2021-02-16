create sequence hibernate_sequence start 1 increment 1;

create table chat (
    id int8 not null,
    organisation_request_id int8,
    physical_request_id int8,
    primary key (id)
);

create table human (
    id int8 not null,
    dob timestamp,
    education varchar(255),
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    passport varchar(255),
    phone_number varchar(255),
    position varchar(255),
    registration_adress varchar(255),
    second_name varchar(255),
    user_id int8,
    organisation_request_id int8,
    passport_giving_organ varchar(255),
    passport_date timestamp,
    citizenship varchar(255),
    group_name varchar (255),
    index varchar (255),
    sex boolean,
    speciality varchar(255),
    primary key (id)
);

create table message (
    id int8 not null,
    text varchar(2048),
    user_id int8,
    chat_id int8,
    time timestamp,
    primary key (id)
);

create table organisation
    (id int8 not null,
    legal_adress varchar(255),
    name varchar(255),
    phone_number varchar(255),
    physical_adress varchar(255),
    director_id int8,
    user_id int8,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    file_name varchar(255),
    is_physical boolean not null,
    password varchar(255),
    username varchar(255),
    organisation_id int8,
    person_id int8,
    primary key (id)
);

create table organisation_request (
    id int8 not null,
    state varchar(255),
    theme varchar(255),
    chat_id int8,
    client_id int8,
    executer_id int8,
    organisation_id int8,
    is_physical boolean not null,
    primary key (id)
);

create table physical_request (
    id int8 not null,
    state varchar(255),
    theme varchar(255),
    chat_id int8,
    client_id int8,
    executer_id int8,
    is_physical boolean not null,
    primary key (id)
);

create table message_file (
    id int8 not null,
    new_file_name varchar(255),
    original_name varchar(255),
    message_id int8, primary key (id)
);
