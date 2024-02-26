create table users
(
    id          bigint       not null auto_increment,
    user_id     varchar(50)  not null unique,
    email       varchar(100) not null,
    nickname    varchar(50)  not null unique,
    password    varchar(100) null,
    thumbnail   varchar(255) null,
    role        varchar(50)  not null,
    created_at  timestamp(6) not null,
    modified_at timestamp(6) not null,
    primary key (id)
);

create table user_provider
(
    id             bigint       not null auto_increment,
    user_id        varchar(50)  not null,
    provider       varchar(50)  not null,
    provider_email varchar(100) not null,
    primary key (id)
);

CREATE INDEX idx_user_id on user_provider (user_id);
ALTER TABLE user_provider
    ADD UNIQUE KEY unique_provider_provider_email (provider, provider_email);
