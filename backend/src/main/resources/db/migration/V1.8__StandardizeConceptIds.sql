INSERT INTO payment_concepts (id, label)
VALUES ('MAINTENANCE', 'MAINTENANCE');
INSERT INTO payment_concepts (id, label)
VALUES ('ACCESS_CHIPS', 'ACCESS_CHIPS');
INSERT INTO payment_concepts (id, label)
VALUES ('PARKING_PEN', 'PARKING_PEN');
INSERT INTO payment_concepts (id, label)
VALUES ('COMMON_AREA_CONSTRUCTION', 'COMMON_AREA_CONSTRUCTION');

INSERT INTO payment_sub_concepts (id, payment_concept_id, label)
VALUES ('MAINTENANCE_BIM_1', 'MAINTENANCE', 'MAINTENANCE_BIM_1');
INSERT INTO payment_sub_concepts (id, payment_concept_id, label)
VALUES ('MAINTENANCE_BIM_2', 'MAINTENANCE', 'MAINTENANCE_BIM_2');
INSERT INTO payment_sub_concepts (id, payment_concept_id, label)
VALUES ('MAINTENANCE_BIM_3', 'MAINTENANCE', 'MAINTENANCE_BIM_3');
INSERT INTO payment_sub_concepts (id, payment_concept_id, label)
VALUES ('MAINTENANCE_BIM_4', 'MAINTENANCE', 'MAINTENANCE_BIM_4');
INSERT INTO payment_sub_concepts (id, payment_concept_id, label)
VALUES ('MAINTENANCE_BIM_5', 'MAINTENANCE', 'MAINTENANCE_BIM_5');
INSERT INTO payment_sub_concepts (id, payment_concept_id, label)
VALUES ('MAINTENANCE_BIM_6', 'MAINTENANCE', 'MAINTENANCE_BIM_6');
INSERT INTO payment_sub_concepts (id, payment_concept_id, label)
VALUES ('MAINTENANCE_ANNUITY', 'MAINTENANCE', 'MAINTENANCE_ANNUITY');

UPDATE payments p
SET payment_concept_id='MAINTENANCE'
FROM payment_concepts pc
WHERE p.payment_concept_id = pc.id
  AND pc.label = 'Mantenimiento';

UPDATE payments p
SET payment_concept_id='ACCESS_CHIPS'
FROM payment_concepts pc
WHERE p.payment_concept_id = pc.id
  AND pc.label = 'Chips';

UPDATE payments p
SET payment_concept_id='PARKING_PEN'
FROM payment_concepts pc
WHERE p.payment_concept_id = pc.id
  AND pc.label = 'Instalación Pluma';

UPDATE payments p
SET payment_concept_id='COMMON_AREA_CONSTRUCTION'
FROM payment_concepts pc
WHERE p.payment_concept_id = pc.id
  AND pc.label = 'Area deportiva';

UPDATE payments p
SET payment_sub_concept_id='MAINTENANCE_BIM_1'
FROM payment_sub_concepts pc
WHERE p.payment_sub_concept_id = pc.id
  AND pc.label = 'Bimestre 1';

UPDATE payments p
SET payment_sub_concept_id='MAINTENANCE_BIM_2'
FROM payment_sub_concepts pc
WHERE p.payment_sub_concept_id = pc.id
  AND pc.label = 'Bimestre 2';

UPDATE payments p
SET payment_sub_concept_id='MAINTENANCE_BIM_3'
FROM payment_sub_concepts pc
WHERE p.payment_sub_concept_id = pc.id
  AND pc.label = 'Bimestre 3';

UPDATE payments p
SET payment_sub_concept_id='MAINTENANCE_BIM_4'
FROM payment_sub_concepts pc
WHERE p.payment_sub_concept_id = pc.id
  AND pc.label = 'Bimestre 4';

UPDATE payments p
SET payment_sub_concept_id='MAINTENANCE_BIM_5'
FROM payment_sub_concepts pc
WHERE p.payment_sub_concept_id = pc.id
  AND pc.label = 'Bimestre 5';

UPDATE payments p
SET payment_sub_concept_id='MAINTENANCE_BIM_6'
FROM payment_sub_concepts pc
WHERE p.payment_sub_concept_id = pc.id
  AND pc.label = 'Bimestre 6';

UPDATE payments p
SET payment_sub_concept_id='MAINTENANCE_ANNUITY'
FROM payment_sub_concepts pc
WHERE p.payment_sub_concept_id = pc.id
  AND pc.label = 'Anualidad';


DELETE
FROM payment_sub_concepts
WHERE id NOT IN ('MAINTENANCE_BIM_1', 'MAINTENANCE_BIM_2', 'MAINTENANCE_BIM_3',
                 'MAINTENANCE_BIM_4', 'MAINTENANCE_BIM_5', 'MAINTENANCE_BIM_6',
                 'MAINTENANCE_ANNUITY');

DELETE
FROM payment_concepts
WHERE id NOT IN ('MAINTENANCE', 'ACCESS_CHIPS', 'PARKING_PEN', 'COMMON_AREA_CONSTRUCTION');

UPDATE payment_concepts SET label = 'Mantenimiento' WHERE id = 'MAINTENANCE';
UPDATE payment_concepts SET label = 'Tarjeta de acceso' WHERE id = 'ACCESS_CHIPS';
UPDATE payment_concepts SET label = 'Pluma' WHERE id = 'PARKING_PEN';
UPDATE payment_concepts SET label = 'Construcción area común' WHERE id = 'COMMON_AREA_CONSTRUCTION';

UPDATE payment_sub_concepts SET label = 'Bimestre 1' WHERE id = 'MAINTENANCE_BIM_1';
UPDATE payment_sub_concepts SET label = 'Bimestre 2' WHERE id = 'MAINTENANCE_BIM_2';
UPDATE payment_sub_concepts SET label = 'Bimestre 3' WHERE id = 'MAINTENANCE_BIM_3';
UPDATE payment_sub_concepts SET label = 'Bimestre 4' WHERE id = 'MAINTENANCE_BIM_4';
UPDATE payment_sub_concepts SET label = 'Bimestre 5' WHERE id = 'MAINTENANCE_BIM_5';
UPDATE payment_sub_concepts SET label = 'Bimestre 6' WHERE id = 'MAINTENANCE_BIM_6';
UPDATE payment_sub_concepts SET label = 'Anualidad' WHERE id = 'MAINTENANCE_ANNUITY';