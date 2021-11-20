package org.uresti.pozarreal.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.Course;
import org.uresti.pozarreal.dto.CourseOccurrence;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.model.ScheduleByCourse;
import org.uresti.pozarreal.repository.CourseRepository;
import org.uresti.pozarreal.repository.ScheduleByCourseRepository;

import java.util.LinkedList;
import java.util.List;

public class CoursesServiceImplTests {

    @Test
    public void givenAnEmptyCoursesList_whenGetCourses_thenEmptyListIsReturned() {
        // Given:
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        ScheduleByCourseRepository scheduleByCourseRepository = Mockito.mock(ScheduleByCourseRepository.class);
        CoursesServiceImpl coursesService = new CoursesServiceImpl(courseRepository, scheduleByCourseRepository);

        LoggedUser user = LoggedUser.builder().build();

        Mockito.when(courseRepository.findAll()).thenReturn(null);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<Course> courses = coursesService.findAll();

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertTrue(courses.isEmpty());
    }

    @Test
    public void givenAnCoursesListWithTwoElements_whenGetCourses_thenListWithTwoElementsIsReturned() {
        // Given:
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        ScheduleByCourseRepository scheduleByCourseRepository = Mockito.mock(ScheduleByCourseRepository.class);
        CoursesServiceImpl coursesService = new CoursesServiceImpl(courseRepository, scheduleByCourseRepository);

        LoggedUser user = LoggedUser.builder().build();
        List<Course> lista = new LinkedList<>();

        Course course1 = new Course();
        course1.setName("Name 1");
        course1.setId("id1");
        course1.setProfessorId("profe1");
        course1.setProfessorName("Profe name 1");
        lista.add(course1);

        Course course2 = new Course();
        course2.setName("Name 2");
        course2.setId("id2");
        course2.setProfessorId("profe2");
        course2.setProfessorName("Profe name 2");
        lista.add(course2);

        Mockito.when(courseRepository.getCoursesInfo()).thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<Course> courses = coursesService.findAll();

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertEquals(2, courses.size());
        Assertions.assertEquals("Name 1", courses.get(0).getName());
        Assertions.assertEquals("id1", courses.get(0).getId());
        Assertions.assertEquals("profe1", courses.get(0).getProfessorId());
        Assertions.assertEquals("Profe name 1", courses.get(0).getProfessorName());
        Assertions.assertEquals("Name 2", courses.get(1).getName());
        Assertions.assertEquals("id2", courses.get(1).getId());
        Assertions.assertEquals("profe2", courses.get(1).getProfessorId());
        Assertions.assertEquals("Profe name 2", courses.get(1).getProfessorName());
    }

    @Test
    public void givenScheduleByCourse_whenGetScheduleByCourse_thenConvertToOccurrenceDto() {
        // Given:
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        ScheduleByCourseRepository scheduleByCourseRepository = Mockito.mock(ScheduleByCourseRepository.class);
        CoursesServiceImpl coursesService = new CoursesServiceImpl(courseRepository, scheduleByCourseRepository);

        LoggedUser user = LoggedUser.builder().build();

        List<Course> lista = new LinkedList<>();
        List<CourseOccurrence> courseOccurrences = new LinkedList<>();
        List<ScheduleByCourse> scheduleByCourses = new LinkedList<>();

        CourseOccurrence courseOccurrence = new CourseOccurrence();
        courseOccurrence.setStartTime("10:00");
        courseOccurrence.setEndTime("12:00");
        courseOccurrence.setLabel("Wednesday");
        courseOccurrence.setId("id1");
        courseOccurrences.add(courseOccurrence);

        Course course1 = new Course();
        course1.setName("Name 1");
        course1.setId("id1");
        course1.setProfessorId("profe1");
        course1.setProfessorName("Profe name 1");
        course1.setProfessorPhone("123456");
        course1.setProfessorAddress("Address");
        course1.setOccurrences(courseOccurrences);
        lista.add(course1);

        ScheduleByCourse scheduleByCourse = new ScheduleByCourse();
        scheduleByCourse.setCourseId("id1");
        scheduleByCourse.setEndTime(1200);
        scheduleByCourse.setStartTime(1000);
        scheduleByCourse.setWeekday("Wednesday");
        scheduleByCourse.setId("id1");
        scheduleByCourses.add(scheduleByCourse);

        Mockito.when(courseRepository.getCoursesInfo()).thenReturn(lista);
        Mockito.when(scheduleByCourseRepository.findAllByCourseId("id1")).thenReturn(scheduleByCourses);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<Course> courses = coursesService.findAll();

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertEquals(1, courses.size());
        Assertions.assertEquals("Name 1", courses.get(0).getName());
        Assertions.assertEquals("id1", courses.get(0).getId());
        Assertions.assertEquals("profe1", courses.get(0).getProfessorId());
        Assertions.assertEquals("Profe name 1", courses.get(0).getProfessorName());
        Assertions.assertEquals(courseOccurrence.getId(), courses.get(0).getOccurrences().get(0).getId());
        Assertions.assertEquals(courseOccurrence.getStartTime(), courses.get(0).getOccurrences().get(0).getStartTime());
        Assertions.assertEquals(courseOccurrence.getEndTime(), courses.get(0).getOccurrences().get(0).getEndTime());
        Assertions.assertEquals(courseOccurrence.getLabel(), courses.get(0).getOccurrences().get(0).getLabel());
        Assertions.assertEquals("10:00", courses.get(0).getOccurrences().get(0).getStartTime());
        Assertions.assertEquals("12:00", courses.get(0).getOccurrences().get(0).getEndTime());
    }

    @Test
    public void givenScheduleByCourseWithWrongTime_whenGetScheduleByCourse_thenConvertToOccurrenceDtoWithWrongTime() {
        // Given:
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        ScheduleByCourseRepository scheduleByCourseRepository = Mockito.mock(ScheduleByCourseRepository.class);
        CoursesServiceImpl coursesService = new CoursesServiceImpl(courseRepository, scheduleByCourseRepository);

        LoggedUser user = LoggedUser.builder().build();

        List<Course> lista = new LinkedList<>();
        List<CourseOccurrence> courseOccurrences = new LinkedList<>();
        List<ScheduleByCourse> scheduleByCourses = new LinkedList<>();

        CourseOccurrence courseOccurrence = new CourseOccurrence();
        courseOccurrence.setStartTime("10:00");
        courseOccurrence.setEndTime("12:00");
        courseOccurrence.setLabel("Wednesday");
        courseOccurrence.setId("id1");
        courseOccurrences.add(courseOccurrence);

        Course course1 = new Course();
        course1.setName("Name 1");
        course1.setId("id1");
        course1.setProfessorId("profe1");
        course1.setProfessorName("Profe name 1");
        course1.setProfessorPhone("123456");
        course1.setProfessorAddress("Address");
        course1.setOccurrences(courseOccurrences);
        lista.add(course1);

        ScheduleByCourse scheduleByCourse = new ScheduleByCourse();
        scheduleByCourse.setCourseId("id1");
        scheduleByCourse.setEndTime(12);
        scheduleByCourse.setStartTime(10);
        scheduleByCourse.setWeekday("Wednesday");
        scheduleByCourse.setId("id1");
        scheduleByCourses.add(scheduleByCourse);

        Mockito.when(courseRepository.getCoursesInfo()).thenReturn(lista);
        Mockito.when(scheduleByCourseRepository.findAllByCourseId("id1")).thenReturn(scheduleByCourses);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<Course> courses = coursesService.findAll();

        String startTime = String.format("%02d:%02d", scheduleByCourse.getStartTime() / 100, scheduleByCourse.getStartTime() % 100);
        String endTime = String.format("%02d:%02d", scheduleByCourse.getEndTime() / 100, scheduleByCourse.getEndTime() % 100);

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertFalse(courses.isEmpty());
        Assertions.assertEquals("Name 1", courses.get(0).getName());
        Assertions.assertEquals("id1", courses.get(0).getId());
        Assertions.assertEquals("profe1", courses.get(0).getProfessorId());
        Assertions.assertEquals("Profe name 1", courses.get(0).getProfessorName());
        Assertions.assertEquals(courseOccurrence.getId(), courses.get(0).getOccurrences().get(0).getId());
        Assertions.assertNotEquals(courseOccurrence.getStartTime(), courses.get(0).getOccurrences().get(0).getStartTime());
        Assertions.assertEquals(courseOccurrence.getLabel(), courses.get(0).getOccurrences().get(0).getLabel());
        Assertions.assertNotSame(startTime, courses.get(0).getOccurrences().get(0).getStartTime());
        Assertions.assertNotSame(endTime, courses.get(0).getOccurrences().get(0).getEndTime());
    }
}