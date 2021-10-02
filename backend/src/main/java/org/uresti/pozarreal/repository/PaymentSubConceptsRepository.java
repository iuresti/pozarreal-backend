package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.PaymentSubConcept;

import java.util.List;

@Repository
public interface PaymentSubConceptsRepository extends JpaRepository<PaymentSubConcept, String> {
    List<PaymentSubConcept> findAllByConceptId(String conceptId);
}
