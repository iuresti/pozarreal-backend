package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.PaymentConcept;
import org.uresti.pozarreal.service.PaymentConceptsService;

import java.util.List;

@RestController
@RequestMapping("/api/paymentConcepts")
@Slf4j
public class PaymentConceptsController {

    private final PaymentConceptsService paymentConceptsService;

    public PaymentConceptsController(PaymentConceptsService paymentConceptsService) {
        this.paymentConceptsService = paymentConceptsService;
    }

    @GetMapping
    public List<PaymentConcept> getPaymentConcepts() {
        return paymentConceptsService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentConcept addPaymentConcept(PaymentConcept paymentConcept) {

        return paymentConceptsService.save(paymentConcept);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping
    public PaymentConcept updatePaymentConcept(PaymentConcept paymentConcept) {

        return paymentConceptsService.update(paymentConcept);
    }
}
