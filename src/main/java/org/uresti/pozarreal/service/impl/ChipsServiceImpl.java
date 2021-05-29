package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.uresti.pozarreal.dto.Chip;
import org.uresti.pozarreal.repository.ChipsRepository;
import org.uresti.pozarreal.service.ChipsService;
import org.uresti.pozarreal.service.mappers.ChipsMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChipsServiceImpl implements ChipsService {

    private final ChipsRepository chipsRepository;

    public ChipsServiceImpl(ChipsRepository chipsRepository) {
        this.chipsRepository = chipsRepository;
    }

    @Override
    public List<Chip> getChipsByHouse(String houseId) {
        return chipsRepository.getChipByHouse(houseId).stream()
                .map(ChipsMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Chip activateChip(String chipId) {
        org.uresti.pozarreal.model.Chip chip = chipsRepository.findById(chipId).orElseThrow();

        chip.setValid(true);

        return ChipsMapper.entityToDto(chipsRepository.save(chip));
    }

    @Override
    public Chip deactivateChip(String chipId) {
        org.uresti.pozarreal.model.Chip chip = chipsRepository.findById(chipId).orElseThrow();

        chip.setValid(false);

        return ChipsMapper.entityToDto(chipsRepository.save(chip));
    }
}
