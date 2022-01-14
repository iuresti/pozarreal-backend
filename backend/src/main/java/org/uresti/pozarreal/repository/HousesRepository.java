package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.House;

import java.util.List;

@Repository
public interface HousesRepository extends JpaRepository<House, String> {

    List<House> findAllByStreetOrderByNumber(String street);

}
