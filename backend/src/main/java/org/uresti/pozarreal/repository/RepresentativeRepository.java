package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Representative;

import java.util.Optional;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, String> {

    Optional<Representative> findByStreet(String street);
}
