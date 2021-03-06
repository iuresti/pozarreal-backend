package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentByConcept {
    String id;
    boolean isComplete;
    double amount = 0;
    boolean validated;
    boolean conflict;
}
