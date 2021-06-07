package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.PaymentConcept;

public class PaymentConceptMapper {

    public static PaymentConcept entityToDto(org.uresti.pozarreal.model.PaymentConcept paymentConcept) {
        return PaymentConcept.builder()
                .id(paymentConcept.getId())
                .label(paymentConcept.getLabel())
                .build();

    }

    public static org.uresti.pozarreal.model.PaymentConcept dtoToEntity(PaymentConcept paymentConcept) {
        return org.uresti.pozarreal.model.PaymentConcept.builder()
                .id(paymentConcept.getId())
                .label(paymentConcept.getLabel())
                .build();

    }
}

