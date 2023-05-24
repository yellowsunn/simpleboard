create table article
(
    article_id  bigint       not null auto_increment,
    title       varchar(200) not null,
    body        text         not null,
    read_count  bigint       not null,
    user_id     bigint       not null,
    created_at  timestamp(6) not null,
    modified_at timestamp(6) not null,
    primary key (article_id)
);

create table article_like
(
    article_id  bigint       not null,
    user_id     bigint       not null,
    created_at  timestamp(6) not null,
    modified_at timestamp(6) not null,
    primary key (article_id, user_id)
);

create table comment
(
    comment_id        bigint       not null auto_increment,
    content           varchar(500) null,
    parent_comment_id bigint null,
    parent_user_id    bigint null,
    image_url         varchar(500) null,
    like_count        bigint       not null,
    article_id        bigint       not null,
    user_id           bigint       not null,
    created_at        timestamp(6) not null,
    modified_at       timestamp(6) not null,
    primary key (comment_id)
);

create table comment_like
(
    comment_id  bigint       not null,
    user_id     bigint       not null,
    created_at  timestamp(6) not null,
    modified_at timestamp(6) not null,
    primary key (comment_id, user_id)
);
