package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.PaymentSubConcept;
import org.uresti.pozarreal.service.PaymentSubConceptsService;

import java.util.List;

@RestController
@RequestMapping("/api/paymentSubConcepts")
@Slf4j
public class PaymentSubConceptsController {

    private final PaymentSubConceptsService paymentSubConceptsService;

    public PaymentSubConceptsController(PaymentSubConceptsService paymentSubConceptsService) {
        this.paymentSubConceptsService = paymentSubConceptsService;
    }

    @GetMapping("/concept/{paymentConceptId}")
    public List<PaymentSubConcept> getPaymentSubConcepts(@PathVariable String paymentConceptId) {
        return paymentSubConceptsService.findAllByConcept(paymentConceptId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentSubConcept addPaymentSubConcept(@RequestBody PaymentSubConcept paymentSubConcept) {

        return paymentSubConceptsService.save(paymentSubConcept);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping
    public PaymentSubConcept updatePaymentSubConcept(@RequestBody PaymentSubConcept paymentSubConcept) {

        return paymentSubConceptsService.update(paymentSubConcept);
    }
}
