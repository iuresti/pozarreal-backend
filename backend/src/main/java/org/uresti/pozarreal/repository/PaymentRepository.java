package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Payment;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findAllByHouseIdAndPaymentConceptId(String houseId, String concept);

    List<Payment> findAllByHouseIdAndPaymentSubConceptIdAndPaymentDateBetween(String houseId, String subConcept, LocalDate startDate, LocalDate endDate);

    @Query("SELECT p FROM Payment p INNER JOIN House h ON p.houseId = h.id INNER JOIN Street s ON h.street = s.id WHERE s.id = :streetId AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findAllByStreetAndPaymentDateBetween(String streetId, LocalDate startDate, LocalDate endDate);
}