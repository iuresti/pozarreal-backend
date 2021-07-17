package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.dto.Course;
import org.uresti.pozarreal.dto.CourseOccurrence;
import org.uresti.pozarreal.model.ScheduleByCourse;
import org.uresti.pozarreal.repository.CourseRepository;
import org.uresti.pozarreal.repository.ScheduleByCourseRepository;
import org.uresti.pozarreal.service.CoursesService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoursesServiceImpl implements CoursesService {

    private final CourseRepository courseRepository;
    private final ScheduleByCourseRepository scheduleByCourseRepository;

    public CoursesServiceImpl(CourseRepository courseRepository,
                              ScheduleByCourseRepository scheduleByCourseRepository) {
        this.courseRepository = courseRepository;
        this.scheduleByCourseRepository = scheduleByCourseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        List<Course> coursesInfo = courseRepository.getCoursesInfo();

        coursesInfo.forEach(course -> course.setOccurrences(scheduleByCourseRepository.findAllByCourseId(course.getId())
                .stream().map(this::convertToOccurrenceDto).collect(Collectors.toList())));

        return coursesInfo;
    }

    private CourseOccurrence convertToOccurrenceDto(ScheduleByCourse scheduleByCourse) {
        return CourseOccurrence.builder()
                .id(scheduleByCourse.getId())
                .label(scheduleByCourse.getWeekday())
                .startTime(String.format("%02d:%02d", scheduleByCourse.getStartTime() / 100, scheduleByCourse.getStartTime() % 100))
                .endTime(String.format("%02d:%02d", scheduleByCourse.getEndTime() / 100, scheduleByCourse.getEndTime() % 100))
                .build();
    }
}
