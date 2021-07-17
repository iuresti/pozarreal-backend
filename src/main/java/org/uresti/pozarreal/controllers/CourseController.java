package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uresti.pozarreal.dto.Course;
import org.uresti.pozarreal.service.CoursesService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Slf4j
public class CourseController {

    private final CoursesService coursesService;

    public CourseController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public List<Course> getCourses(){
        return coursesService.findAll();
    }
}
