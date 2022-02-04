ALTER TABLE users
    ADD COLUMN status BOOLEAN DEFAULT TRUE;

UPDATE users SET status = true;