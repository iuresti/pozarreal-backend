package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private String id;

    @Column(name = "house_id")
    private String houseId;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "user_id")
    private String userId;

    private Double amount;

    @Column(name = "payment_concept_id")
    private String paymentConceptId;

    @Column(name = "payment_sub_concept_id")
    private String paymentSubConceptId;

    @Column(name = "payment_mode")
    private String paymentMode;

    private String notes;

    private boolean validated;

}
