package org.uresti.pozarreal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
    Optional<Login> findByEmail(String email);
}
