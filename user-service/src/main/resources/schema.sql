create table users
(
    id        bigint      not null auto_increment,
    user_id   varchar(50) not null unique,
    nickname  varchar(50) not null,
    thumbnail varchar(255) null,
    primary key (id)
);

create table user_provider
(
    id       bigint       not null auto_increment,
    user_id  varchar(50)  not null unique,
    email    varchar(100) not null,
    password varchar(100) null,
    provider varchar(50)  not null,
    primary key (id),
    unique key uinque_user_id_and_email (user_id, email),
    unique key unique_email_and_provider (email, provider)
);
