package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.CoursePayment;

import java.util.List;

public interface CourseAssistantPaymentService {
    List<CoursePayment> findAllByCourseAssistant(String courseAssistantId);

    CoursePayment save(CoursePayment coursePayment);

    void delete(String courseAssistantPaymentId);
}
