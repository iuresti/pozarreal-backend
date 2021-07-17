package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("SELECT new org.uresti.pozarreal.dto.Course(c.id, c.name, p.id, p.name, p.phone, p.address) FROM Course c JOIN Professor p ON c.professorId = p.id")
    List<org.uresti.pozarreal.dto.Course> getCoursesInfo();
}
