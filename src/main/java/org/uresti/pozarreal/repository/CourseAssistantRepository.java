package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.CourseAssistant;

import java.util.List;

@Repository
public interface CourseAssistantRepository extends JpaRepository<CourseAssistant, String> {
    List<CourseAssistant> findAllByCourseId(String courseId);
}
