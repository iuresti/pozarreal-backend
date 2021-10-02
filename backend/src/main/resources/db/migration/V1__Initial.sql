CREATE TABLE users
(
    id varchar(40) primary key,
    name varchar(100) not null,
    picture varchar(300)
);

CREATE TABLE logins
(
   id varchar(40) primary key,
   email varchar(200) unique ,
   user_id varchar(40) references users
);

CREATE TABLE roles_by_user
(
    id varchar(40) primary key,
    user_id varchar(40) references users,
    role varchar(50)
);

CREATE TABLE streets
(
    id varchar(40) primary key,
    name varchar(30) not null unique
);

CREATE TABLE houses
(
    id varchar(40) primary key,
    street varchar(40) references streets not null,
    number varchar(10) not null,
    chips_enabled boolean not null default false
);

CREATE UNIQUE INDEX house_number ON houses(street, number);

CREATE TABLE houses_by_user
(
    id varchar(40) primary key,
    userId varchar(40) references users,
    houseId varchar(40) references houses
);

CREATE UNIQUE INDEX houses_by_user_unique ON houses_by_user(userId, houseId);

CREATE TABLE representatives
(
    id varchar(40) primary key,
    street varchar(40) references streets not null unique,
    name varchar(80) not null,
    phone varchar(20) not null,
    address varchar(100) not null
);

CREATE TABLE chips
(
    id varchar(40) primary key,
    house varchar(40) references houses not null,
    code varchar(30) not null,
    valid boolean not null default true,
    notes varchar(500)
);
