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

    public static final String MAINTENANCE = "Mantenimiento";
    public static final String PARKING_PEN = "Instalación Pluma";

    @Id
    private String id;
    private String label;
}
