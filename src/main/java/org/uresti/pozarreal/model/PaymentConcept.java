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

    public static final String MAINTENANCE = "MAINTENANCE";
    public static final String PARKING_PEN = "PARKING_PEN";
    public static final String ACCESS_CHIPS = "ACCESS_CHIPS";
    public static final String COMMON_AREA_CONSTRUCTION = "COMMON_AREA_CONSTRUCTION";

    @Id
    private String id;
    private String label;
}
