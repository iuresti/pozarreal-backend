package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.PaymentSubConcept;

public class PaymentSubConceptMapper {

    public static PaymentSubConcept entityToDto(org.uresti.pozarreal.model.PaymentSubConcept paymentSubConcept) {
        return PaymentSubConcept.builder()
                .id(paymentSubConcept.getId())
                .label(paymentSubConcept.getLabel())
                .conceptId(paymentSubConcept.getConceptId())
                .build();

    }

    public static org.uresti.pozarreal.model.PaymentSubConcept dtoToEntity(PaymentSubConcept paymentSubConcept) {
        return org.uresti.pozarreal.model.PaymentSubConcept.builder()
                .id(paymentSubConcept.getId())
                .label(paymentSubConcept.getLabel())
                .conceptId(paymentSubConcept.getConceptId())
                .build();

    }
}

