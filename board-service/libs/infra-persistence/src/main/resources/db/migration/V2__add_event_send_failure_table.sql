create table event_send_failure
(
    id          bigint        not null auto_increment,
    data        varchar(1024) not null,
    topic       varchar(300)  not null,
    is_used     boolean       not null,
    created_at  timestamp(6)  not null,
    modified_at timestamp(6)  not null,
    primary key (id)
);
