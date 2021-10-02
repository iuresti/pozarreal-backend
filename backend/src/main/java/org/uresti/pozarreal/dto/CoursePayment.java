package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePayment {
    private String id;
    private LocalDate paymentDate;
    private String userId;
    private String courseAssistantId;
    private String concept;
    private BigDecimal amount;
    private String receiptNumber;
    private String notes;
}
