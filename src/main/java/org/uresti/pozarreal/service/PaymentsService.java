package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;

import java.util.List;

public interface PaymentsService {
    List<PaymentView> getPayments(PaymentFilter paymentFilter);
}
