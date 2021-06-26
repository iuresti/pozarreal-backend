package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.uresti.pozarreal.dto.Payment;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;
import org.uresti.pozarreal.repository.CustomPaymentRepository;
import org.uresti.pozarreal.repository.PaymentRepository;
import org.uresti.pozarreal.service.PaymentsService;
import org.uresti.pozarreal.service.mappers.PaymentMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    private final CustomPaymentRepository customPaymentRepository;
    private final PaymentRepository paymentRepository;

    public PaymentsServiceImpl(CustomPaymentRepository customPaymentRepository,
                               PaymentRepository paymentRepository) {
        this.customPaymentRepository = customPaymentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<PaymentView> getPayments(PaymentFilter paymentFilter) {
        return customPaymentRepository.executeQuery(paymentFilter);
    }

    @Override
    public Payment save(Payment payment, String userId) {

        payment.setId(UUID.randomUUID().toString());

        payment.setRegistrationDate(LocalDate.now());
        payment.setUserId(userId);

        return PaymentMapper.entityToDto(paymentRepository.save(PaymentMapper.dtoToEntity(payment)));
    }
}
