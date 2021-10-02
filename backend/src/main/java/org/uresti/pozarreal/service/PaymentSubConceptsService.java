package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.PaymentSubConcept;

import java.util.List;

public interface PaymentSubConceptsService {
    List<PaymentSubConcept> findAllByConcept(String paymentConceptId);

    PaymentSubConcept save(PaymentSubConcept paymentSubConcept);

    PaymentSubConcept update(PaymentSubConcept paymentSubConcept);

}
