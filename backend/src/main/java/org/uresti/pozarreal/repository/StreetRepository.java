package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Street;

@Repository
public interface StreetRepository extends JpaRepository<Street, String> {
}
