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

    public static final String MAINTENANCE_BIM_1 = "MAINTENANCE_BIM_1";
    public static final String MAINTENANCE_BIM_2 = "MAINTENANCE_BIM_2";
    public static final String MAINTENANCE_BIM_3 = "MAINTENANCE_BIM_3";
    public static final String MAINTENANCE_BIM_4 = "MAINTENANCE_BIM_4";
    public static final String MAINTENANCE_BIM_5 = "MAINTENANCE_BIM_5";
    public static final String MAINTENANCE_BIM_6 = "MAINTENANCE_BIM_6";
    public static final String MAINTENANCE_ANNUITY = "MAINTENANCE_ANNUITY";

    @Id
    private String id;
    @Column(name = "payment_concept_id")
    private String conceptId;
    private String label;
}
