package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.uresti.pozarreal.dto.CoursePayment;
import org.uresti.pozarreal.repository.CourseAssistantPaymentRepository;
import org.uresti.pozarreal.service.CourseAssistantPaymentService;
import org.uresti.pozarreal.service.mappers.CourseAssistantPaymentMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseAssistantPaymentServiceImpl implements CourseAssistantPaymentService {

    private final CourseAssistantPaymentRepository courseAssistantPaymentRepository;

    public CourseAssistantPaymentServiceImpl(CourseAssistantPaymentRepository courseAssistantPaymentRepository) {
        this.courseAssistantPaymentRepository = courseAssistantPaymentRepository;
    }

    @Override
    public List<CoursePayment> findAllByCourseAssistant(String courseAssistantId) {
        return courseAssistantPaymentRepository.findAllByCourseAssistantIdOrderByPaymentDateDesc(courseAssistantId)
                .stream().limit(10).map(CourseAssistantPaymentMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CoursePayment save(CoursePayment coursePayment) {
        if (coursePayment.getId() == null) {
            coursePayment.setId(UUID.randomUUID().toString());
        }

        return CourseAssistantPaymentMapper.entityToDto(courseAssistantPaymentRepository.save(CourseAssistantPaymentMapper.dtoToEntity(coursePayment)));
    }

    @Override
    public void delete(String courseAssistantPaymentId) {
        courseAssistantPaymentRepository.deleteById(courseAssistantPaymentId);
    }
}
