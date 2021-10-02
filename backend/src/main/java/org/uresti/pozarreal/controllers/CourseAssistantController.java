package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.CourseAssistant;
import org.uresti.pozarreal.service.CourseAssistantService;

import java.util.List;

@RestController
@RequestMapping("/api/course-assistants")
@Slf4j
public class CourseAssistantController {

    private final CourseAssistantService courseAssistantService;


    public CourseAssistantController(CourseAssistantService courseAssistantService) {
        this.courseAssistantService = courseAssistantService;
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public List<CourseAssistant> findAllByCourse(@PathVariable String courseId) {
        return courseAssistantService.findAllByCourse(courseId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public CourseAssistant save(@RequestBody CourseAssistant courseAssistant) {
        return courseAssistantService.save(courseAssistant);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public CourseAssistant update(@RequestBody CourseAssistant courseAssistant) {
        return courseAssistantService.save(courseAssistant);
    }

    @DeleteMapping("/{courseAssistantId}")
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public void delete(@PathVariable String courseAssistantId) {
        courseAssistantService.delete(courseAssistantId);
    }
}
