package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.PaymentConcept;

import java.util.List;

public interface PaymentConceptsService {
    List<PaymentConcept> findAll();

    PaymentConcept save(PaymentConcept paymentConcept);

    PaymentConcept update(PaymentConcept paymentConcept);

}
