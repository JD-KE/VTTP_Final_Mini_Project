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

create table events (
    id char(8) not null,
    name varchar(128) not null,
    description text not null,
    details text,
    date_start bigint not null,
    date_end bigint,
    user_created char(8),
    
    primary key(id),
    constraint fk_user_id_for_events foreign key(user_created) references users(id)
);

create table eventgames (
	id char(8) not null,
    game_id int not null,
    game_name varchar(256),
    game_cover_url varchar(256),
    event_id char(8) not null,
    
    
    primary key(id),
    constraint fk_event_id foreign key(event_id) references events(id)
);

grant all privileges on eventHallDb.* to fred@'%';
flush privileges;