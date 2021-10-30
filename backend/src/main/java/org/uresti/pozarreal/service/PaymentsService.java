package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.Payment;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;

import java.security.Principal;
import java.util.List;

public interface PaymentsService {
    List<PaymentView> getPayments(PaymentFilter paymentFilter);

    Payment save(Payment payment, Principal principal);

    void delete(String paymentId);

    Payment getPayment(String paymentId, String userIdForLoggedUser);
}
