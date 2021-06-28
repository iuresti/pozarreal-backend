package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Representative;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, String> {

    Representative findRepresentativeByStreet(String street);

    Representative findByUserId(String userId);
}
