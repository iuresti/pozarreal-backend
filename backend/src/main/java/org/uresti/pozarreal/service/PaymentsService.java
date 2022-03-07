package org.uresti.pozarreal.service;

import org.springframework.data.domain.Page;
import org.uresti.pozarreal.dto.Payment;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;

import java.security.Principal;

public interface PaymentsService {
    Page<PaymentView> getPayments(PaymentFilter paymentFilter, int page);

    Payment save(Payment payment, Principal principal);

    void delete(String paymentId, Principal principal);

    Payment getPayment(String paymentId, String userIdForLoggedUser);

    Payment validatePayment(String paymentId);

    Payment conflictPayment(String paymentId);
}
