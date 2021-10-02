package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.CoursePayment;

import java.util.List;

@Repository
public interface CourseAssistantPaymentRepository extends JpaRepository<CoursePayment, String> {
    List<CoursePayment> findAllByCourseAssistantIdOrderByPaymentDateDesc(String courseAssistantId);
}
