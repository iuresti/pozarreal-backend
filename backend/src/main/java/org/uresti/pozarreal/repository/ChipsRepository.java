package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Chip;

import java.util.List;

@Repository
public interface ChipsRepository extends JpaRepository<Chip, String> {

    List<Chip> getChipByHouse(String house);

    @Query("SELECT new Chip(chip.id, chip.house, chip.code, CASE house.chipsEnabled WHEN false then false ELSE chip.valid END as chipsEnabled, chip.notes) FROM Chip chip INNER JOIN House house ON chip.house = house.id")
    List<Chip> getChipsWithRealStatus();
}
