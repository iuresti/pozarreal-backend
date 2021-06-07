package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;
import org.uresti.pozarreal.repository.CustomPaymentRepository;
import org.uresti.pozarreal.service.PaymentsService;

import java.util.List;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    private final CustomPaymentRepository customPaymentRepository;

    public PaymentsServiceImpl(CustomPaymentRepository customPaymentRepository) {
        this.customPaymentRepository = customPaymentRepository;
    }

    @Override
    public List<PaymentView> getPayments(PaymentFilter paymentFilter) {
        return customPaymentRepository.executeQuery(paymentFilter);
    }
}
