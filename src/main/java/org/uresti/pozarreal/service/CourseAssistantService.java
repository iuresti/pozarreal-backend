package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.CourseAssistant;

import java.util.List;

public interface CourseAssistantService {
    List<CourseAssistant> findAllByCourse(String courseId);

    CourseAssistant save(CourseAssistant courseAssistant);

    void delete(String courseAssistantId);
}
