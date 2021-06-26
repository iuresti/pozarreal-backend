package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.Payment;

public class PaymentMapper {
    public static org.uresti.pozarreal.model.Payment dtoToEntity(Payment paymentDto) {
        return org.uresti.pozarreal.model.Payment.builder()
                .id(paymentDto.getId())
                .amount(paymentDto.getAmount())
                .houseId(paymentDto.getHouseId())
                .notes(paymentDto.getNotes())
                .paymentConceptId(paymentDto.getPaymentConceptId())
                .paymentDate(paymentDto.getPaymentDate())
                .paymentMode(paymentDto.getPaymentMode())
                .registrationDate(paymentDto.getRegistrationDate())
                .userId(paymentDto.getUserId())
                .build();
    }

    public static Payment entityToDto(org.uresti.pozarreal.model.Payment paymentModel) {
        return Payment.builder()
                .id(paymentModel.getId())
                .amount(paymentModel.getAmount())
                .houseId(paymentModel.getHouseId())
                .notes(paymentModel.getNotes())
                .paymentConceptId(paymentModel.getPaymentConceptId())
                .paymentDate(paymentModel.getPaymentDate())
                .paymentMode(paymentModel.getPaymentMode())
                .registrationDate(paymentModel.getRegistrationDate())
                .userId(paymentModel.getUserId())
                .build();
    }

}
