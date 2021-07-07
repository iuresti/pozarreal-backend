package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findAllByHouseIdAndPaymentConceptId(String houseId, String concept);

    List<Payment> findAllByHouseIdAndPaymentSubConceptId(String houseId, String subConcept);

    @Query("SELECT p FROM Payment p INNER JOIN House h ON p.houseId = h.id INNER JOIN Street s ON h.street = s.id WHERE s.id = :streetId AND p.paymentDate >= :startDate")
    List<Payment> findAllByStreetAndPaymentDateIsGreaterThanEqual(String streetId, LocalDate startDate);
}