package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.HouseInfo;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.House;
import org.uresti.pozarreal.model.Representative;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.repository.HousesRepository;
import org.uresti.pozarreal.repository.RepresentativeRepository;
import org.uresti.pozarreal.repository.StreetRepository;
import org.uresti.pozarreal.service.HousesService;
import org.uresti.pozarreal.service.mappers.HousesMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HousesServiceImpl implements HousesService {

    private final HousesRepository housesRepository;
    private final StreetRepository streetRepository;
    private final RepresentativeRepository representativeRepository;
    private final SessionHelper sessionHelper;


    public HousesServiceImpl(HousesRepository housesRepository,
                             StreetRepository streetRepository,
                             RepresentativeRepository representativeRepository,
                             SessionHelper sessionHelper) {
        this.housesRepository = housesRepository;
        this.streetRepository = streetRepository;
        this.representativeRepository = representativeRepository;
        this.sessionHelper = sessionHelper;
    }

    @Override
    @Transactional
    public void toggleChipStatusRequest(String houseId, boolean enable) {
        House house = housesRepository.findById(houseId).orElseThrow();

        house.setChipsEnabled(enable);

        housesRepository.save(house);
    }

    @Override
    @Transactional(readOnly = true)
    public List<org.uresti.pozarreal.dto.House> getHousesByStreet(String streetId, LoggedUser user) {

        if (sessionHelper.hasRole(user, Role.ROLE_REPRESENTATIVE) && !sessionHelper.hasRole(user, Role.ROLE_ADMIN)) {
            Representative representative = representativeRepository.findById(user.getUserId()).orElseThrow();

            if (!representative.getStreet().equals(streetId)) {
                throw new BadRequestDataException("Invalid street for representative query", "INVALID_STREET");
            }
        }

        return housesRepository.findAllByStreetOrderByNumber(streetId).stream()
                .map(HousesMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public HouseInfo getHouseInfo(String houseId) {

        House house = housesRepository.findById(houseId).orElseThrow();
        Street street = streetRepository.findById(house.getStreet()).orElseThrow();

        return HouseInfo.builder()
                .id(house.getId())
                .number(house.getNumber())
                .street(house.getStreet())
                .streetName(street.getName())
                .chipsEnabled(house.isChipsEnabled())
                .notes(house.getNotes())
                .build();
    }

    @Override
    public void saveNotes(String houseId, String notes) {
        House house = housesRepository.findById(houseId).orElseThrow();

        house.setNotes(notes);

        housesRepository.save(house);
    }
}
