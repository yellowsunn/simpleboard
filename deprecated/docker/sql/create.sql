create table userEntity (
	user_id bigint not null auto_increment,
    username varchar(50),
    password varchar(512),
    role varchar(50),
    created_date datetime,
    primary key (user_id),
    constraint username_unique unique (username)
);

create table posts (
	post_id bigint not null auto_increment,
    title varchar(1000) not null,
    content text not null,
    type varchar(50) not null,
    user_id bigint,
    created_date datetime,
    last_modified_date datetime,
    primary key (post_id),
    foreign key (user_id) references userEntity(user_id)
);

create table post_hit (
	post_hit_id bigint not null auto_increment,
    hit bigint not null,
    post_id bigint not null,
    primary key (post_hit_id),
    foreign key (post_id) references posts(post_id)
);

create table comment (
	comment_id bigint not null auto_increment,
    content varchar(3000) not null,
    parent_id bigint,
    user_id bigint,
    post_id bigint not null,
    created_date datetime,
    primary key (comment_id),
    foreign key (parent_id) references comment(comment_id),
    foreign key (user_id) references userEntity(user_id),
    foreign key (post_id) references posts(post_id)
);

create table file (
	file_id bigint not null auto_increment,
    store_name varchar(500) not null,
    post_id bigint not null,
    primary key (file_id),
    foreign key (post_id) references posts(post_id)
);

alter table comment add index idx_post_parent_id (post_id, parent_id);
alter table posts add index idx_type_and_id(type, post_id);
