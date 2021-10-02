package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.PaymentConcept;

@Repository
public interface PaymentConceptsRepository extends JpaRepository<PaymentConcept, String> {

}
