ALTER TABLE payments
    ADD COLUMN validated boolean DEFAULT FALSE;

UPDATE payments SET validated = TRUE;