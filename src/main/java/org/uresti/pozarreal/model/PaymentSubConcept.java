package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_sub_concepts")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSubConcept {

    public static final String MAINTENANCE_TWO_MONTHS_1 = "Bimestre 1";
    public static final String MAINTENANCE_TWO_MONTHS_2 = "Bimestre 2";
    public static final String MAINTENANCE_TWO_MONTHS_3 = "Bimestre 3";
    public static final String MAINTENANCE_TWO_MONTHS_4 = "Bimestre 4";
    public static final String MAINTENANCE_TWO_MONTHS_5 = "Bimestre 5";
    public static final String MAINTENANCE_TWO_MONTHS_6 = "Bimestre 6";
    public static final String MAINTENANCE_ANNUITY = "Anualidad";

    @Id
    private String id;
    @Column(name = "payment_concept_id")
    private String conceptId;
    private String label;
}
