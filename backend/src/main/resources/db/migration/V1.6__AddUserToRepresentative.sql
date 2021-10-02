ALTER TABLE representatives
    ADD COLUMN  user_id varchar(40) references users;
