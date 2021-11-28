package org.uresti.pozarreal.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uresti.pozarreal.dto.Course;
import org.uresti.pozarreal.dto.CourseOccurrence;
import org.uresti.pozarreal.model.ScheduleByCourse;
import org.uresti.pozarreal.repository.CourseRepository;
import org.uresti.pozarreal.repository.ScheduleByCourseRepository;

import java.util.LinkedList;
import java.util.List;

public class CoursesServiceImplTests {

    @Test
    public void givenAnEmptyCoursesList_whenFindAll_thenEmptyListIsReturned() {
        // Given:
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        ScheduleByCourseRepository scheduleByCourseRepository = Mockito.mock(ScheduleByCourseRepository.class);

        CoursesServiceImpl coursesService = new CoursesServiceImpl(courseRepository, scheduleByCourseRepository);

        Mockito.when(courseRepository.findAll()).thenReturn(null);

        // When:
        List<Course> courses = coursesService.findAll();

        // Then:
        Assertions.assertThat(courses.isEmpty()).isTrue();
    }

    @Test
    public void givenAnCoursesListWithTwoElements_whenFindAll_thenListWithTwoElementsIsReturned() {
        // Given:
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        ScheduleByCourseRepository scheduleByCourseRepository = Mockito.mock(ScheduleByCourseRepository.class);

        CoursesServiceImpl coursesService = new CoursesServiceImpl(courseRepository, scheduleByCourseRepository);

        List<Course> courses = new LinkedList<>();

        Course course1 = Course.builder()
                .name("Name 1")
                .id("id1")
                .professorId("profe1")
                .professorName("Profe name 1")
                .build();

        Course course2 = Course.builder()
                .name("Name 2")
                .id("id2")
                .professorId("profe2")
                .professorName("Profe name 2")
                .build();

        courses.add(course1);
        courses.add(course2);

        Mockito.when(courseRepository.getCoursesInfo()).thenReturn(courses);

        // When:
        List<Course> courseList = coursesService.findAll();

        // Then:
        Assertions.assertThat(courseList.size()).isEqualTo(2);
        Assertions.assertThat(courseList.get(0).getName()).isEqualTo("Name 1");
        Assertions.assertThat(courseList.get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(courseList.get(0).getProfessorId()).isEqualTo("profe1");
        Assertions.assertThat(courseList.get(0).getProfessorName()).isEqualTo("Profe name 1");
        Assertions.assertThat(courseList.get(1).getName()).isEqualTo("Name 2");
        Assertions.assertThat(courseList.get(1).getId()).isEqualTo("id2");
        Assertions.assertThat(courseList.get(1).getProfessorId()).isEqualTo("profe2");
        Assertions.assertThat(courseList.get(1).getProfessorName()).isEqualTo("Profe name 2");
    }

    @Test
    public void givenScheduleByCourse_whenConvertToOccurrenceDto_thenConvertToOccurrenceDto() {
        // Given:
        CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
        ScheduleByCourseRepository scheduleByCourseRepository = Mockito.mock(ScheduleByCourseRepository.class);

        CoursesServiceImpl coursesService = new CoursesServiceImpl(courseRepository, scheduleByCourseRepository);

        List<Course> courses = new LinkedList<>();
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

        courses.add(course1);

        ScheduleByCourse scheduleByCourse = new ScheduleByCourse();
        scheduleByCourse.setCourseId("id1");
        scheduleByCourse.setEndTime(1200);
        scheduleByCourse.setStartTime(1000);
        scheduleByCourse.setWeekday("Wednesday");
        scheduleByCourse.setId("id1");
        scheduleByCourses.add(scheduleByCourse);

        Mockito.when(courseRepository.getCoursesInfo()).thenReturn(courses);
        Mockito.when(scheduleByCourseRepository.findAllByCourseId("id1")).thenReturn(scheduleByCourses);

        // When:
        List<Course> courseList = coursesService.findAll();

        // Then:
        Assertions.assertThat(courseList.size()).isEqualTo(1);
        Assertions.assertThat(courseList.get(0).getName()).isEqualTo("Name 1");
        Assertions.assertThat(courseList.get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(courseList.get(0).getProfessorId()).isEqualTo("profe1");
        Assertions.assertThat(courseList.get(0).getProfessorName()).isEqualTo("Profe name 1");
        Assertions.assertThat(courseList.get(0).getOccurrences().get(0).getId()).isEqualTo(courseOccurrence.getId());
        Assertions.assertThat(courseList.get(0).getOccurrences().get(0).getStartTime()).isEqualTo(courseOccurrence.getStartTime());
        Assertions.assertThat(courseList.get(0).getOccurrences().get(0).getEndTime()).isEqualTo(courseOccurrence.getEndTime());
        Assertions.assertThat(courseList.get(0).getOccurrences().get(0).getLabel()).isEqualTo(courseOccurrence.getLabel());
        Assertions.assertThat(courseList.get(0).getOccurrences().get(0).getStartTime()).isEqualTo("10:00");
        Assertions.assertThat(courseList.get(0).getOccurrences().get(0).getEndTime()).isEqualTo("12:00");
    }
}