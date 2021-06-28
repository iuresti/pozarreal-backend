package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.model.House;
import org.uresti.pozarreal.repository.HousesRepository;
import org.uresti.pozarreal.repository.PaymentConceptsRepository;
import org.uresti.pozarreal.repository.PaymentRepository;
import org.uresti.pozarreal.repository.PaymentSubConceptsRepository;
import org.uresti.pozarreal.service.HousesService;
import org.uresti.pozarreal.service.mappers.HousesMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HousesServiceImpl implements HousesService {

    private final HousesRepository housesRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentSubConceptsRepository paymentSubConceptsRepository;
    private final PaymentConceptsRepository paymentConceptsRepository;

    private final PozarrealConfig pozarrealConfig;

    public HousesServiceImpl(HousesRepository housesRepository,
                             PaymentRepository paymentRepository,
                             PaymentSubConceptsRepository paymentSubConceptsRepository,
                             PaymentConceptsRepository paymentConceptsRepository,
                             PozarrealConfig pozarrealConfig) {
        this.housesRepository = housesRepository;
        this.paymentRepository = paymentRepository;
        this.paymentSubConceptsRepository = paymentSubConceptsRepository;
        this.paymentConceptsRepository = paymentConceptsRepository;
        this.pozarrealConfig = pozarrealConfig;
    }

    @Override
    @Transactional
    public void toggleChipStatusRequest(String houseId, boolean enable) {
        House house = housesRepository.findById(houseId).orElseThrow();

        house.setChipsEnabled(enable);

        housesRepository.save(house);
    }

    @Override
    public List<org.uresti.pozarreal.dto.House> getHousesByStreet(String streetId) {
        return housesRepository.findAllByStreetOrderByNumber(streetId).stream()
                .map(HousesMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
