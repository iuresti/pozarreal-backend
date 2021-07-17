package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "course_payment")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePayment {

    @Id
    private String id;
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "course_assistant_id")
    private String courseAssistantId;
    private String concept;
    private BigDecimal amount;
    @Column(name = "receipt_number")
    private String receiptNumber;
    private String notes;
}
