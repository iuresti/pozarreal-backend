package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.Payment;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;
import org.uresti.pozarreal.service.PaymentsService;

import java.security.Principal;

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
    public Page<PaymentView> search(PaymentFilter paymentFilter, @RequestParam("page") Integer page) {
        return paymentsService.getPayments(paymentFilter, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public Payment save(@RequestBody Payment payment, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Saving payment (house: {}, amount: $ {}, concept: {}, user: {} - {})", payment.getHouseId(), payment.getAmount(), payment.getPaymentConceptId(), loggedUser.getName(), loggedUser.getUserId());

        return paymentsService.save(payment, principal);
    }

    @GetMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Payment save(@PathVariable String paymentId, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Getting payment (paymentId: {}, user: {} - {})", paymentId, loggedUser.getName(), loggedUser.getUserId());

        return paymentsService.getPayment(paymentId, sessionHelper.getUserIdForLoggedUser(principal));
    }

    @DeleteMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public void deletePayment(@PathVariable String paymentId, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Deleting payment (paymentId: {}, user: {} - {})", paymentId, loggedUser.getName(), loggedUser.getUserId());

        paymentsService.delete(paymentId, principal);
    }

    @PatchMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Payment validatePayment(Principal principal, @PathVariable String paymentId) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Updating payment: {} by user: {} - {}", paymentId, loggedUser.getName(), loggedUser.getUserId());

        return paymentsService.validatePayment(paymentId);
    }

    @PutMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ROLE_REPRESENTATIVE')")
    public Payment conflictPayment(Principal principal, @PathVariable String paymentId) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Updating payment: {} by user: {} - {}", paymentId, loggedUser.getName(), loggedUser.getUserId());

        return paymentsService.conflictPayment(paymentId);
    }
}
