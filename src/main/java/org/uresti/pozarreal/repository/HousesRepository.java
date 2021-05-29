package org.uresti.pozarreal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.House;

@Repository
public interface HousesRepository extends JpaRepository<House, String> {

    List<House> findAllByStreetOrderByNumber(String street);
}
