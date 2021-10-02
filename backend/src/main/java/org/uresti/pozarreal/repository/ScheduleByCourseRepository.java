package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.ScheduleByCourse;

import java.util.List;

@Repository
public interface ScheduleByCourseRepository extends JpaRepository<ScheduleByCourse, String> {

    List<ScheduleByCourse> findAllByCourseId(String courseId);
}
