package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Payment;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findAllByHouseIdAndPaymentDateIsGreaterThanEqual(String houseId, LocalDate paymentDateReference);

    @Query("SELECT p FROM Payment p INNER JOIN PaymentConcept pc ON p.paymentConceptId = pc.id WHERE pc.label = :concept AND p.houseId = :houseId")
    List<Payment> findAllByHouseIdAndPaymentConcept(String houseId, String concept);
}