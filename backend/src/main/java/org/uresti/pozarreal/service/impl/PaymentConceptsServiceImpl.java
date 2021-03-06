package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.dto.PaymentConcept;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.repository.PaymentConceptsRepository;
import org.uresti.pozarreal.service.PaymentConceptsService;
import org.uresti.pozarreal.service.mappers.PaymentConceptMapper;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentConceptsServiceImpl implements PaymentConceptsService {

    private final PaymentConceptsRepository paymentConceptsRepository;

    public PaymentConceptsServiceImpl(PaymentConceptsRepository paymentConceptsRepository) {
        this.paymentConceptsRepository = paymentConceptsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentConcept> findAll() {
        return paymentConceptsRepository.findAll().stream()
                .map(PaymentConceptMapper::entityToDto)
                .sorted(Comparator.comparing(PaymentConcept::getLabel))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentConcept save(PaymentConcept paymentConcept) {

        paymentConcept.setId(UUID.randomUUID().toString());

        return PaymentConceptMapper.entityToDto(paymentConceptsRepository.save(PaymentConceptMapper.dtoToEntity(paymentConcept)));

    }

    @Override
    @Transactional
    public PaymentConcept update(PaymentConcept paymentConcept) {
        if (paymentConcept.getId() == null) {
            throw new BadRequestDataException("Missing payment concept id", "REQUIRED_PAYMENT_CONCEPT_ID");
        }

        return PaymentConceptMapper.entityToDto(paymentConceptsRepository.save(PaymentConceptMapper.dtoToEntity(paymentConcept)));
    }
}
