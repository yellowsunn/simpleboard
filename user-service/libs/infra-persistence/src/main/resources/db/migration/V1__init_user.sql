create table users
(
    user_id     bigint       not null auto_increment,
    nickname    varchar(50)  not null,
    email       varchar(100) not null,
    password    varchar(100) null,
    uuid        varchar(50)  not null,
    thumbnail   varchar(255) null,
    role        varchar(50)  not null,
    created_at  timestamp(6) not null,
    modified_at timestamp(6) not null,
    primary key (user_id),
    unique key uk_email (email),
    unique key uk_uuid (uuid),
    unique key uk_nickname (nickname)
);
CREATE INDEX idx_email ON users (email);

create table user_provider
(
    user_provider_id bigint       not null auto_increment,
    user_id          bigint      not null,
    provider         varchar(50)  not null,
    provider_email    varchar(100) not null,
    primary key (user_provider_id)
);

CREATE INDEX idx_user_id_and_provider on user_provider(user_id, provider);
