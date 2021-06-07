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

    private String streetName;

    private String houseNumber;

    private LocalDate paymentDate;

    private LocalDate registrationDate;

    private String userName;

    private Double amount;

    private String paymentConcept;

    private String paymentMode;

    private String notes;

}
