package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.HousesByUser;

@Repository
public interface HousesByUserRepository extends JpaRepository<HousesByUser, String> {
}
