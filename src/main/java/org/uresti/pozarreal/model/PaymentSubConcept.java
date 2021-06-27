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
    @Id
    private String id;
    @Column(name = "payment_concept_id")
    private String conceptId;
    private String label;
}
