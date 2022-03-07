package org.uresti.pozarreal.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.Payment;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;
import org.uresti.pozarreal.exception.MaintenanceFeeOverPassedException;
import org.uresti.pozarreal.exception.PozarrealSystemException;
import org.uresti.pozarreal.repository.CustomPaymentRepository;
import org.uresti.pozarreal.repository.PaymentRepository;
import org.uresti.pozarreal.service.PaymentsService;
import org.uresti.pozarreal.service.mappers.PaymentMapper;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.uresti.pozarreal.model.PaymentConcept.MAINTENANCE;
import static org.uresti.pozarreal.model.PaymentSubConcept.MAINTENANCE_ANNUITY;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    private final CustomPaymentRepository customPaymentRepository;
    private final PaymentRepository paymentRepository;
    private final PozarrealConfig pozarrealConfig;
    private final SessionHelper sessionHelper;

    public PaymentsServiceImpl(CustomPaymentRepository customPaymentRepository,
                               PaymentRepository paymentRepository,
                               SessionHelper sessionHelper,
                               PozarrealConfig pozarrealConfig) {
        this.customPaymentRepository = customPaymentRepository;
        this.paymentRepository = paymentRepository;
        this.pozarrealConfig = pozarrealConfig;
        this.sessionHelper = sessionHelper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentView> getPayments(PaymentFilter paymentFilter, Integer page) {
        return customPaymentRepository.executeQuery(paymentFilter, page);
    }

    @Override
    @Transactional
    public Payment save(Payment payment, Principal principal) {
        if (payment.isValidated()) {
            throw new PozarrealSystemException("Editing validated payment", "INVALID_UPDATE_PAYMENT");
        }

        if (payment.getId() == null) {
            payment.setId(UUID.randomUUID().toString());
        }

        if (payment.getPaymentConceptId().equals(MAINTENANCE)) {
            validateMaintenancePayment(payment, payment.getPaymentDate().getYear());
        }

        if (sessionHelper.hasRole(sessionHelper.getLoggedUser(principal), Role.ROLE_ADMIN)) {
            payment.setValidated(true);
        }

        payment.setRegistrationDate(LocalDate.now());
        payment.setUserId(sessionHelper.getUserIdForLoggedUser(principal));

        return PaymentMapper.entityToDto(paymentRepository.save(PaymentMapper.dtoToEntity(payment)));
    }

    @Override
    @Transactional
    public void delete(String paymentId, Principal principal) {
        org.uresti.pozarreal.model.Payment payment = paymentRepository.findById(paymentId).orElseThrow();

        if (payment.isValidated()) {
            throw new PozarrealSystemException("Deleting payment validated", "INVALID_DELETE_PAYMENT");
        }

        if (sessionHelper.hasRole(sessionHelper.getLoggedUser(principal), Role.ROLE_ADMIN)) {
            paymentRepository.deleteById(paymentId);
        }
    }

    @Override
    public Payment getPayment(String paymentId, String userId) {
        return PaymentMapper.entityToDto(paymentRepository.findById(paymentId).orElseThrow());
    }

    @Override
    public Payment validatePayment(String paymentId) {
        org.uresti.pozarreal.model.Payment payment = paymentRepository.findById(paymentId).orElseThrow();

        payment.setValidated(true);

        return PaymentMapper.entityToDto(paymentRepository.save(payment));
    }

    @Override
    public Payment conflictPayment(String paymentId) {
        org.uresti.pozarreal.model.Payment payment = paymentRepository.findById(paymentId).orElseThrow();

        payment.setConflict(true);

        return PaymentMapper.entityToDto(paymentRepository.save(payment));
    }

    private void validateMaintenancePayment(Payment payment, int startYear) {
        if (!MAINTENANCE_ANNUITY.equals(payment.getPaymentSubConceptId())) {

            LocalDate startOfYear = LocalDate.now().withDayOfYear(1).withYear(startYear);

            LocalDate endOfYear = LocalDate.now().withDayOfYear(1).withYear(startYear + 1);

            List<org.uresti.pozarreal.model.Payment> payments = paymentRepository
                    .findAllByHouseIdAndPaymentSubConceptIdAndPaymentDateBetween(payment.getHouseId(),
                            payment.getPaymentSubConceptId(), startOfYear, endOfYear);

            double totalPayments = payment.getAmount() + payments.stream()
                    .filter(paymentIt -> payment.getId() == null || !paymentIt.getId().equals(payment.getId()))
                    .map(org.uresti.pozarreal.model.Payment::getAmount)
                    .reduce(0.0, Double::sum);

            double overPassed = totalPayments - pozarrealConfig.getFees().getBiMonthlyMaintenanceFee();

            if (overPassed > 0) {
                throw new MaintenanceFeeOverPassedException("Over passed maintenance fee", "ERROR_OVER_PASS_PAYMENT", overPassed);
            }
        }
    }
}
