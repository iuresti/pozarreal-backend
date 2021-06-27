package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.uresti.pozarreal.dto.PaymentSubConcept;
import org.uresti.pozarreal.exception.PozarrealSystemException;
import org.uresti.pozarreal.repository.PaymentSubConceptsRepository;
import org.uresti.pozarreal.service.PaymentSubConceptsService;
import org.uresti.pozarreal.service.mappers.PaymentSubConceptMapper;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentSubConceptsServiceImpl implements PaymentSubConceptsService {

    private final PaymentSubConceptsRepository paymentSubConceptsRepository;

    public PaymentSubConceptsServiceImpl(PaymentSubConceptsRepository paymentSubConceptsRepository) {
        this.paymentSubConceptsRepository = paymentSubConceptsRepository;
    }

    @Override
    public List<PaymentSubConcept> findAllByConcept(String paymentConceptId) {
        return paymentSubConceptsRepository.findAllByConceptId(paymentConceptId).stream()
                .map(PaymentSubConceptMapper::entityToDto)
                .sorted(Comparator.comparing(PaymentSubConcept::getLabel))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentSubConcept save(PaymentSubConcept paymentConcept) {

        paymentConcept.setId(UUID.randomUUID().toString());

        return PaymentSubConceptMapper.entityToDto(paymentSubConceptsRepository.save(PaymentSubConceptMapper.dtoToEntity(paymentConcept)));

    }

    @Override
    public PaymentSubConcept update(PaymentSubConcept paymentConcept) {
        if (paymentConcept.getId() == null) {
            throw new PozarrealSystemException("Campo id de PaymentSubConcept requerido");
        }

        return PaymentSubConceptMapper.entityToDto(paymentSubConceptsRepository.save(PaymentSubConceptMapper.dtoToEntity(paymentConcept)));
    }
}
