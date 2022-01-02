package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentView {

    private String id;

    private String streetId;

    private String streetName;

    private String houseId;

    private String houseNumber;

    private LocalDate paymentDate;

    private LocalDate registrationDate;

    private String userName;

    private Double amount;

    private String paymentConceptId;

    private String paymentConcept;

    private String paymentSubConceptId;

    private String paymentSubConcept;

    private String paymentMode;

    private String notes;

    private boolean validated;
}
