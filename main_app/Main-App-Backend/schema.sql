drop database if exists eventHallDb;

create database eventHallDb;

use eventHallDb;

create table users (
    id char(8) not null,
    username varchar(128) not null,
    email varchar(128) not null,
    password varchar(128) not null,
    role varchar(16) not null,
    -- below is short syntax
    primary key(id)
);

create table tokens (
    id char(8) not null,
    token varchar(512) not null,
    token_type varchar(32) not null,
    revoked boolean not null default false,
    expired boolean not null default false,
    user_id char(8) not null,
    
    primary key(id),
    constraint fk_user_id foreign key(user_id) references users(id)
);

grant all privileges on eventHallDb.* to fred@'%';
flush privileges;