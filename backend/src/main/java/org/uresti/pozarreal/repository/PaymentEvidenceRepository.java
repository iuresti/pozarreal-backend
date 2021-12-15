package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.PaymentEvidence;

@Repository
public interface PaymentEvidenceRepository extends JpaRepository<PaymentEvidence, String>{
}
