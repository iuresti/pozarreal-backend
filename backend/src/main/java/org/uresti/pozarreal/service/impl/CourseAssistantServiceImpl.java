package org.uresti.pozarreal.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.dto.CourseAssistant;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.repository.CourseAssistantRepository;
import org.uresti.pozarreal.service.CourseAssistantService;
import org.uresti.pozarreal.service.mappers.CourseAssistantMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseAssistantServiceImpl implements CourseAssistantService {

    private final CourseAssistantRepository courseAssistantRepository;

    public CourseAssistantServiceImpl(CourseAssistantRepository courseAssistantRepository) {
        this.courseAssistantRepository = courseAssistantRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseAssistant> findAllByCourse(String courseId) {
        return courseAssistantRepository.findAllByCourseId(courseId).stream()
                .map(CourseAssistantMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseAssistant save(CourseAssistant courseAssistant) {

        if (StringUtils.isEmpty(courseAssistant.getCourseId())) {
            throw new BadRequestDataException("Invalid course id for course assistant", "INVALID_COURSE_ID");
        }

        if (StringUtils.isEmpty(courseAssistant.getName())) {
            throw new BadRequestDataException("Name is required for course assistant", "INVALID_COURSE_ASSISTANT_NAME");
        }

        if (courseAssistant.getId() == null) {
            courseAssistant.setId(UUID.randomUUID().toString());
        }

        return CourseAssistantMapper.entityToDto(courseAssistantRepository.save(CourseAssistantMapper.dtoToEntity(courseAssistant)));
    }

    @Override
    @Transactional
    public void delete(String courseAssistantId) {
        courseAssistantRepository.deleteById(courseAssistantId);
    }
}
