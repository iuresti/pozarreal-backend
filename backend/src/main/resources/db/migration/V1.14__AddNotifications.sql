CREATE TABLE notifications
(
    id        varchar(40) primary key,
    timestamp timestamp,
    message   varchar(1000),
    seen      boolean default false,
    user_id   varchar(40) references users
)