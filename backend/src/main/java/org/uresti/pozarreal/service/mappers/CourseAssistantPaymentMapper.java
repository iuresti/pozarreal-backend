package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.CoursePayment;

public class CourseAssistantPaymentMapper {

    public static CoursePayment entityToDto(org.uresti.pozarreal.model.CoursePayment coursePayment) {
        return CoursePayment.builder()
                .id(coursePayment.getId())
                .paymentDate(coursePayment.getPaymentDate())
                .receiptNumber(coursePayment.getReceiptNumber())
                .courseAssistantId(coursePayment.getCourseAssistantId())
                .amount(coursePayment.getAmount())
                .concept(coursePayment.getConcept())
                .userId(coursePayment.getUserId())
                .notes(coursePayment.getNotes())
                .build();
    }

    public static org.uresti.pozarreal.model.CoursePayment dtoToEntity(CoursePayment coursePayment) {
        return org.uresti.pozarreal.model.CoursePayment.builder()
                .id(coursePayment.getId())
                .id(coursePayment.getId())
                .paymentDate(coursePayment.getPaymentDate())
                .receiptNumber(coursePayment.getReceiptNumber())
                .courseAssistantId(coursePayment.getCourseAssistantId())
                .amount(coursePayment.getAmount())
                .concept(coursePayment.getConcept())
                .userId(coursePayment.getUserId())
                .notes(coursePayment.getNotes())
                .build();
    }
}
