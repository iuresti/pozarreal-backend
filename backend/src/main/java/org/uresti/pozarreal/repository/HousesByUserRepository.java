package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.HouseByUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface HousesByUserRepository extends JpaRepository<HouseByUser, String> {
    List<HouseByUser> findAllByUserId(String userId);

    Optional<HouseByUser> findByHouseId(String s);
}
