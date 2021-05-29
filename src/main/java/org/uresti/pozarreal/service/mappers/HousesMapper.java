package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.House;

public class HousesMapper {

    public static House entityToDto(org.uresti.pozarreal.model.House houseModel) {
        return House.builder()
                .id(houseModel.getId())
                .chipsEnabled(houseModel.isChipsEnabled())
                .number(houseModel.getNumber())
                .street(houseModel.getStreet())
                .build();
    }

}
