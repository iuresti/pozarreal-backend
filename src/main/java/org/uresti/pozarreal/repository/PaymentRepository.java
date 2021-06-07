package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
