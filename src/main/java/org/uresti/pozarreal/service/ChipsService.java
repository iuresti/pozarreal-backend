package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.Chip;

import java.util.List;

public interface ChipsService {

    List<Chip> getChipsByHouse(String houseId);

    Chip activateChip(String chipId);

    Chip deactivateChip(String chipId);

    Chip addChip(Chip chip);

    Chip deleteChip(String chipId);
}
