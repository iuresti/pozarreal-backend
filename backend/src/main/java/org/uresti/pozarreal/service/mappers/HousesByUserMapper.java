package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.HouseByUser;

public class HousesByUserMapper {

    public static HouseByUser entityToDto(org.uresti.pozarreal.model.HouseByUser houseByUser) {
        return HouseByUser.builder()
                .id(houseByUser.getId())
                .userId(houseByUser.getUserId())
                .houseId(houseByUser.getHouseId())
                .mainHouse(houseByUser.getMainHouse())
                .validated(houseByUser.getValidated())
                .build();
    }

    public static org.uresti.pozarreal.model.HouseByUser dtoToEntity(HouseByUser houseByUser) {
        return org.uresti.pozarreal.model.HouseByUser.builder()
                .id(houseByUser.getId())
                .userId(houseByUser.getUserId())
                .houseId(houseByUser.getHouseId())
                .mainHouse(houseByUser.getMainHouse())
                .validated(houseByUser.getValidated())
                .build();
    }
}

