package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.Payment;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;
import org.uresti.pozarreal.service.PaymentsService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentsController {

    private final PaymentsService paymentsService;
    private final SessionHelper sessionHelper;

    public PaymentsController(PaymentsService paymentsService, SessionHelper sessionHelper) {
        this.paymentsService = paymentsService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<PaymentView> search(PaymentFilter paymentFilter) {
        return paymentsService.getPayments(paymentFilter);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment save(@RequestBody Payment payment, Principal principal) {
        return paymentsService.save(payment, sessionHelper.getUserIdForLoggedUser(principal));
    }
}
