package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Chip;

import java.util.List;

@Repository
public interface ChipsRepository extends JpaRepository<Chip, String> {

    List<Chip> getChipByHouse(String house);
}
