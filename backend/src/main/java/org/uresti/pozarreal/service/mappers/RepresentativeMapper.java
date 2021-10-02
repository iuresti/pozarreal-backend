package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.Representative;

public class RepresentativeMapper {

    public static Representative entityToDto(org.uresti.pozarreal.model.Representative representativeModel) {
        return Representative.builder()
                .userId(representativeModel.getUserId())
                .street(representativeModel.getStreet())
                .phone(representativeModel.getPhone())
                .house(representativeModel.getHouse())
                .build();

    }

    public static org.uresti.pozarreal.model.Representative dtoToEntity(Representative representative) {
        return org.uresti.pozarreal.model.Representative.builder()
                .userId(representative.getUserId())
                .street(representative.getStreet())
                .phone(representative.getPhone())
                .house(representative.getHouse())
                .build();
    }
}
