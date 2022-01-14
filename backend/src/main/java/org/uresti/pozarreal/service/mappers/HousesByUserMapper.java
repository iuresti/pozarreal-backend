package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.HousesByUser;

public class HousesByUserMapper {

    public static HousesByUser entityToDto(org.uresti.pozarreal.model.HousesByUser housesByUser) {
        return HousesByUser.builder()
                .id(housesByUser.getId())
                .userId(housesByUser.getUserId())
                .houseId(housesByUser.getHouseId())
                .mainHouse(housesByUser.getMainHouse())
                .validated(housesByUser.getValidated())
                .build();
    }

    public static org.uresti.pozarreal.model.HousesByUser dtoToEntity(HousesByUser housesByUser) {
        return org.uresti.pozarreal.model.HousesByUser.builder()
                .id(housesByUser.getId())
                .userId(housesByUser.getUserId())
                .houseId(housesByUser.getHouseId())
                .mainHouse(housesByUser.getMainHouse())
                .validated(housesByUser.getValidated())
                .build();
    }
}

