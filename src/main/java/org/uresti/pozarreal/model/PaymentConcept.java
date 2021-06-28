package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_concepts")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConcept {

    public static final String MAINTENENCE = "Mantenimiento";

    @Id
    private String id;
    private String label;
}
