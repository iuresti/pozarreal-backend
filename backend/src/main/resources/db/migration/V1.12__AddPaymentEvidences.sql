CREATE TABLE payment_evidences
(
    id         varchar(40) primary key,
    payment_id varchar(40),
    url        varchar(1000)
);