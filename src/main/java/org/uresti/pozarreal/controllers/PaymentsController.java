package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.LoggedUser;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public List<PaymentView> search(PaymentFilter paymentFilter) {
        return paymentsService.getPayments(paymentFilter);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Payment save(@RequestBody Payment payment, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Saving payment (house: {}, amount: $ {}, concept: {}, user: {} - {})", payment.getHouseId(), payment.getAmount(), payment.getPaymentConceptId(), loggedUser.getName(), loggedUser.getUserId());

        return paymentsService.save(payment, sessionHelper.getUserIdForLoggedUser(principal));
    }

    @DeleteMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deletePayment(@PathVariable String paymentId, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Deleting payment (paymentId: {}, user: {} - {})", paymentId, loggedUser.getName(), loggedUser.getUserId());

        paymentsService.delete(paymentId);
    }
}
