package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.Representative;
import org.uresti.pozarreal.repository.RepresentativeRepository;
import org.uresti.pozarreal.service.RepresentativesService;
import org.uresti.pozarreal.service.mappers.RepresentativeMapper;

@Service
public class RepresentativesServiceImpl implements RepresentativesService {

    private final RepresentativeRepository representativeRepository;

    public RepresentativesServiceImpl(RepresentativeRepository representativeRepository) {
        this.representativeRepository = representativeRepository;
    }

    @Override
    public org.uresti.pozarreal.dto.Representative save(org.uresti.pozarreal.dto.Representative representative, String userId) {

        if (!representative.getUserId().equals(userId)) {
            throw new BadRequestDataException("UserId does not match with request body to update representative", "WRONG_USER_ID");
        }

        return RepresentativeMapper.entityToDto(representativeRepository.save(RepresentativeMapper.dtoToEntity(representative)));
    }

    @Override
    public void delete(String userId) {
        Representative representative = representativeRepository.findByUserId(userId);

        if (representative != null) {
            representativeRepository.delete(representative);
        }
    }

    @Override
    public org.uresti.pozarreal.dto.Representative saveStreet(String userId, String streetId) {
        Representative representative = representativeRepository.findById(userId)
                .orElse(Representative.builder().userId(userId).build());

        representative.setStreet(streetId);

        return RepresentativeMapper.entityToDto(representativeRepository.save(representative));
    }
}
