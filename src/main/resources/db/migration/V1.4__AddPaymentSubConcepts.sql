CREATE TABLE payment_sub_concepts
(
    id varchar(40) primary key,
    payment_concept_id varchar(40) references payment_concepts not null,
    label varchar(40) unique not null
);

ALTER TABLE payments
    ADD COLUMN  payment_sub_concept_id varchar(40) references payment_sub_concepts;

