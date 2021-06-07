CREATE TABLE payment_concepts
(
    id varchar(40) primary key,
    label varchar(40) unique not null
);

CREATE TABLE payments
(
    id varchar(40) primary key,
    house_id varchar(40) references houses not null,
    payment_date date not null,
    registration_date date not null default CURRENT_DATE,
    user_id varchar(40) references users,
    amount decimal(10,2) not null,
    payment_concept_id varchar(40) references payment_concepts,
    payment_mode varchar(50),
    notes varchar(300)
);