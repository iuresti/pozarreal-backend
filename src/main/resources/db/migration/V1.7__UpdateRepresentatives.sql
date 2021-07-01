DROP TABLE representatives;

CREATE TABLE representatives
(
    user_id varchar(40) primary key references users,
    street varchar(40) references streets not null unique,
    phone varchar(20) null,
    house varchar(40) references houses null
);
