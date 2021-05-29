package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.model.House;
import org.uresti.pozarreal.repository.HousesRepository;
import org.uresti.pozarreal.service.HousesService;

@Service
public class HousesServiceImpl implements HousesService {

    private final HousesRepository housesRepository;

    public HousesServiceImpl(HousesRepository housesRepository) {
        this.housesRepository = housesRepository;
    }

    @Override
    @Transactional
    public void toggleChipStatusRequest(String houseId, boolean enable) {
        House house = housesRepository.findById(houseId).orElseThrow();

        house.setChipsEnabled(enable);

        housesRepository.save(house);
    }
}
