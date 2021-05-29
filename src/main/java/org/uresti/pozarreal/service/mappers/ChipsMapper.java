package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.Chip;

public class ChipsMapper {

    public static Chip entityToDto(org.uresti.pozarreal.model.Chip chipModel) {
        return Chip.builder()
                .id(chipModel.getId())
                .code(chipModel.getCode())
                .house(chipModel.getHouse())
                .notes(chipModel.getNotes())
                .valid(chipModel.isValid())
                .build();
    }

    public static org.uresti.pozarreal.model.Chip dtoToEntity(Chip chipDto) {
        return org.uresti.pozarreal.model.Chip.builder()
                .id(chipDto.getId())
                .code(chipDto.getCode())
                .house(chipDto.getHouse())
                .notes(chipDto.getNotes())
                .valid(chipDto.isValid())
                .build();
    }
}
