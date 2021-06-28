CREATE INDEX payment_house_payment_date ON payments (house_id, payment_date);

CREATE INDEX payment_house ON payments (house_id)