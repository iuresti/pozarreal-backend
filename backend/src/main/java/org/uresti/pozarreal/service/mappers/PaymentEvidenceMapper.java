package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.PaymentEvidence;

public class PaymentEvidenceMapper {

    public static PaymentEvidence entityToDto(org.uresti.pozarreal.model.PaymentEvidence paymentEvidence) {
        return PaymentEvidence.builder()
                .paymentId(paymentEvidence.getPaymentId())
                .url(paymentEvidence.getUrl())
                .build();

    }

    public static org.uresti.pozarreal.model.PaymentEvidence dtoToEntity(PaymentEvidence paymentEvidence) {
        return org.uresti.pozarreal.model.PaymentEvidence.builder()
                .paymentId(paymentEvidence.getPaymentId())
                .url(paymentEvidence.getUrl())
                .build();

    }
}

