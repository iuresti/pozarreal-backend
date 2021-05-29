package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.Representative;

public class RepresentativeMapper {

    public static Representative entityToDto(org.uresti.pozarreal.model.Representative representativeModel) {
        return Representative.builder()
                .id(representativeModel.getId())
                .name(representativeModel.getName())
                .address(representativeModel.getAddress())
                .phone(representativeModel.getPhone())
                .street(representativeModel.getStreet())
                .build();

    }
}
