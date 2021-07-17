package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.dto.CoursePayment;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.service.CourseAssistantPaymentService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/course-assistants-payment")
@Slf4j
public class CourseAssistantPaymentController {

    private final CourseAssistantPaymentService courseAssistantPaymentService;
    private final SessionHelper sessionHelper;


    public CourseAssistantPaymentController(CourseAssistantPaymentService courseAssistantPaymentService,
                                            SessionHelper sessionHelper) {
        this.courseAssistantPaymentService = courseAssistantPaymentService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping("/assistant/{courseAssistantId}")
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public List<CoursePayment> findAllByCourseAssistant(@PathVariable String courseAssistantId) {
        return courseAssistantPaymentService.findAllByCourseAssistant(courseAssistantId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public CoursePayment save(@RequestBody CoursePayment coursePayment, Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        coursePayment.setUserId(loggedUser.getUserId());

        return courseAssistantPaymentService.save(coursePayment);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public CoursePayment update(@RequestBody CoursePayment coursePayment) {
        return courseAssistantPaymentService.save(coursePayment);
    }

    @DeleteMapping("/{courseAssistantPaymentId}")
    @PreAuthorize("hasAnyRole('ROLE_SCHOOL_MANAGER')")
    public void delete(@PathVariable String courseAssistantPaymentId) {
        courseAssistantPaymentService.delete(courseAssistantPaymentId);
    }
}
