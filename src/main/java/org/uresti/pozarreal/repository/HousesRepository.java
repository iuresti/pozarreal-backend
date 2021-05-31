package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.House;

import java.util.List;

@Repository
public interface HousesRepository extends JpaRepository<House, String> {

    List<House> findAllByStreetOrderByNumber(String street);

    @Query(nativeQuery = true, value = "SELECT h.* FROM houses h INNER JOIN houses_by_user u ON h.id = u.houseId AND u.userId = :userId")
    List<House> findHousesByUser(String userId);
}
