ALTER TABLE payments
    ADD COLUMN conflict boolean DEFAULT FALSE;

UPDATE payments SET conflict = FALSE;