package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.HousesByUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface HousesByUserRepository extends JpaRepository<HousesByUser, String> {
    List<HousesByUser> findAllByUserId(String userId);

    Optional<HousesByUser> findByHouseId(String s);
}
