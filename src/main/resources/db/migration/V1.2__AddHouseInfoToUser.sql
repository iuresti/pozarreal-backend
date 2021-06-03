ALTER TABLE users
    DROP COLUMN house_id;

ALTER TABLE houses_by_user
    ADD COLUMN main_house boolean default false,
    ADD COLUMN validated boolean default false;