package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.dto.StreetInfo;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.repository.HousesRepository;
import org.uresti.pozarreal.repository.RepresentativeRepository;
import org.uresti.pozarreal.repository.StreetRepository;
import org.uresti.pozarreal.service.StreetsService;
import org.uresti.pozarreal.service.mappers.HousesMapper;
import org.uresti.pozarreal.service.mappers.RepresentativeMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StreetServiceImpl implements StreetsService {

    private final StreetRepository streetRepository;

    private final RepresentativeRepository representativeRepository;

    private final HousesRepository housesRepository;

    public StreetServiceImpl(StreetRepository streetRepository,
                             RepresentativeRepository representativeRepository,
                             HousesRepository housesRepository) {
        this.streetRepository = streetRepository;
        this.representativeRepository = representativeRepository;
        this.housesRepository = housesRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Street> getStreets() {
        return streetRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public StreetInfo getStreetInfo(String streetId) {

        Street street = streetRepository.findById(streetId).orElseThrow();
        StreetInfo streetInfo = new StreetInfo();

        streetInfo.setId(streetId);
        streetInfo.setName(street.getName());
        streetInfo.setRepresentative(RepresentativeMapper.entityToDto(representativeRepository.findRepresentativeByStreet(streetId)));
        streetInfo.setHouses(housesRepository.findAllByStreetOrderByNumber(streetId).stream().map(HousesMapper::entityToDto).collect(Collectors.toList()));

        return streetInfo;
    }
}
