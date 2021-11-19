package org.uresti.pozarreal.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.CourseAssistant;
import org.uresti.pozarreal.repository.*;
import org.uresti.pozarreal.service.mappers.CourseAssistantMapper;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class CourseAssistantServiceImplTests {

    @Test
    public void givenAnEmptyCourseAssistant_whenGetCourseAssistant_thenEmptyListIsReturned() {
        // Given:
        CourseAssistantRepository courseAssistantRepository = Mockito.mock(CourseAssistantRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantServiceImpl courseAssistantService = new CourseAssistantServiceImpl(courseAssistantRepository);
        LoggedUser user = LoggedUser.builder().build();

        List<CourseAssistant> lista = new LinkedList<>();

        Mockito.when(courseAssistantRepository.findAll()).thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_ADMIN)).thenReturn(true);

        // When:
        List<org.uresti.pozarreal.dto.CourseAssistant> courseAssistants = courseAssistantService.findAllByCourse(null);

        // Then:
        Assertions.assertTrue(courseAssistants.isEmpty());
    }

    @Test
    public void givenAnECourseAssistantWithOneElement_whenGetCourseAssistant_thenListWithOneElementIsReturned() {
        // Given:
        CourseAssistantRepository courseAssistantRepository = Mockito.mock(CourseAssistantRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantServiceImpl courseAssistantService = new CourseAssistantServiceImpl(courseAssistantRepository);
        LoggedUser user = LoggedUser.builder().build();

        List<CourseAssistant> lista = new LinkedList<>();
        LocalDate now = LocalDate.now();

        CourseAssistant courseAssistant1 = new CourseAssistant();
        courseAssistant1.setCourseId("course1");
        courseAssistant1.setId("id1");
        courseAssistant1.setNotes("hello");
        courseAssistant1.setEmail("email@email.com");
        courseAssistant1.setBirthDate(now);
        courseAssistant1.setPhone("123456789");
        courseAssistant1.setName("name");
        courseAssistant1.setResponsibleName("name1");
        courseAssistant1.setResponsibleName2("name2");
        courseAssistant1.setResponsiblePhone("phone1");
        courseAssistant1.setResponsiblePhone2("phone2");
        lista.add(courseAssistant1);

        Mockito.when(courseAssistantRepository.findAllByCourseId("course1")).thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<org.uresti.pozarreal.dto.CourseAssistant> courseAssistants = courseAssistantService.findAllByCourse("course1");

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertEquals(1, courseAssistants.size());
        Assertions.assertEquals("course1", courseAssistants.get(0).getCourseId());
        Assertions.assertEquals("id1", courseAssistants.get(0).getId());
        Assertions.assertEquals("hello", courseAssistants.get(0).getNotes());
        Assertions.assertEquals("email@email.com", courseAssistants.get(0).getEmail());
        Assertions.assertEquals(now, courseAssistants.get(0).getBirthDate());
        Assertions.assertEquals("123456789", courseAssistants.get(0).getPhone());
        Assertions.assertEquals("name", courseAssistants.get(0).getName());
        Assertions.assertEquals("name1", courseAssistants.get(0).getResponsibleName());
        Assertions.assertEquals("name2", courseAssistants.get(0).getResponsibleName2());
        Assertions.assertEquals("phone1", courseAssistants.get(0).getResponsiblePhone());
        Assertions.assertEquals("phone2", courseAssistants.get(0).getResponsiblePhone2());
    }

    @Test
    public void givenAnECourseAssistantWithOneElement_whenGetCourseAssistant_thenIsSaved() {
        // Given:
        CourseAssistantRepository courseAssistantRepository = Mockito.mock(CourseAssistantRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantServiceImpl courseAssistantService = new CourseAssistantServiceImpl(courseAssistantRepository);
        LoggedUser user = LoggedUser.builder().build();

        LocalDate now = LocalDate.now();

        CourseAssistant courseAssistant1 = new CourseAssistant();
        courseAssistant1.setCourseId("course1");
        courseAssistant1.setId("id1");
        courseAssistant1.setNotes("hello");
        courseAssistant1.setEmail("email@email.com");
        courseAssistant1.setBirthDate(now);
        courseAssistant1.setPhone("123456789");
        courseAssistant1.setName("name");
        courseAssistant1.setResponsibleName("name1");
        courseAssistant1.setResponsibleName2("name2");
        courseAssistant1.setResponsiblePhone("phone1");
        courseAssistant1.setResponsiblePhone2("phone2");

        Mockito.when(courseAssistantRepository.save(courseAssistant1)).thenReturn(courseAssistant1);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        org.uresti.pozarreal.dto.CourseAssistant courseAssistant = courseAssistantService.save(CourseAssistantMapper.entityToDto(courseAssistant1));

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertNotNull(courseAssistant);
        Assertions.assertEquals("course1", courseAssistant.getCourseId());
        Assertions.assertEquals("id1", courseAssistant.getId());
        Assertions.assertEquals("hello", courseAssistant.getNotes());
        Assertions.assertEquals("email@email.com", courseAssistant.getEmail());
        Assertions.assertEquals(now, courseAssistant.getBirthDate());
        Assertions.assertEquals("123456789", courseAssistant.getPhone());
        Assertions.assertEquals("name", courseAssistant.getName());
        Assertions.assertEquals("name1", courseAssistant.getResponsibleName());
        Assertions.assertEquals("name2", courseAssistant.getResponsibleName2());
        Assertions.assertEquals("phone1", courseAssistant.getResponsiblePhone());
        Assertions.assertEquals("phone2", courseAssistant.getResponsiblePhone2());
    }

    @Test
    public void givenAnECourseAssistantWithOutCourseId_whenGetCourseAssistant_thenThrowBadRequestDataException() {
        // Given:
        CourseAssistantRepository courseAssistantRepository = Mockito.mock(CourseAssistantRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantServiceImpl courseAssistantService = new CourseAssistantServiceImpl(courseAssistantRepository);
        LoggedUser user = LoggedUser.builder().build();

        LocalDate now = LocalDate.now();

        CourseAssistant courseAssistant1 = new CourseAssistant();
        courseAssistant1.setId("id1");
        courseAssistant1.setNotes("hello");
        courseAssistant1.setEmail("email@email.com");
        courseAssistant1.setBirthDate(now);
        courseAssistant1.setName("name");
        courseAssistant1.setPhone("123456789");
        courseAssistant1.setResponsibleName("name1");
        courseAssistant1.setResponsibleName2("name2");
        courseAssistant1.setResponsiblePhone("phone1");
        courseAssistant1.setResponsiblePhone2("phone2");

        Mockito.when(courseAssistantRepository.save(courseAssistant1))
                .thenThrow(new BadRequestDataException("Invalid course id for course assistant", "INVALID_COURSE_ID"));
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        org.uresti.pozarreal.dto.CourseAssistant courseAssistant = null;
        try {
            courseAssistant = courseAssistantService.save(CourseAssistantMapper.entityToDto(courseAssistant1));

        } catch (BadRequestDataException ignored) {
        }

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertNull(courseAssistant);
    }

    @Test
    public void givenAnECourseAssistantWithOutName_whenGetCourseAssistant_thenThrowBadRequestDataException() {
        // Given:
        CourseAssistantRepository courseAssistantRepository = Mockito.mock(CourseAssistantRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantServiceImpl courseAssistantService = new CourseAssistantServiceImpl(courseAssistantRepository);
        LoggedUser user = LoggedUser.builder().build();

        LocalDate now = LocalDate.now();

        CourseAssistant courseAssistant1 = new CourseAssistant();
        courseAssistant1.setCourseId("course1");
        courseAssistant1.setId("id1");
        courseAssistant1.setNotes("hello");
        courseAssistant1.setEmail("email@email.com");
        courseAssistant1.setBirthDate(now);
        courseAssistant1.setPhone("123456789");
        courseAssistant1.setResponsibleName("name1");
        courseAssistant1.setResponsibleName2("name2");
        courseAssistant1.setResponsiblePhone("phone1");
        courseAssistant1.setResponsiblePhone2("phone2");

        Mockito.when(courseAssistantRepository.save(courseAssistant1))
                .thenThrow(new BadRequestDataException("Name is required for course assistant", "INVALID_COURSE_ASSISTANT_NAME"));
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        org.uresti.pozarreal.dto.CourseAssistant courseAssistant = null;
        try {
            courseAssistant = courseAssistantService.save(CourseAssistantMapper.entityToDto(courseAssistant1));

        } catch (BadRequestDataException ignored) {
        }

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertNull(courseAssistant);
    }

    @Test
    public void givenAnECourseAssistantWithEmptyId_whenGetCourseAssistant_thenIsReturnedAnListWithOneElementAndIsFilled() {
        // Given:
        CourseAssistantRepository courseAssistantRepository = Mockito.mock(CourseAssistantRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantServiceImpl courseAssistantService = new CourseAssistantServiceImpl(courseAssistantRepository);
        LoggedUser user = LoggedUser.builder().build();

        List<CourseAssistant> lista = new LinkedList<>();
        LocalDate now = LocalDate.now();

        CourseAssistant courseAssistant1 = new CourseAssistant();
        courseAssistant1.setCourseId("course1");
        courseAssistant1.setNotes("hello");
        courseAssistant1.setEmail("email@email.com");
        courseAssistant1.setBirthDate(now);
        courseAssistant1.setPhone("123456789");
        courseAssistant1.setName("name");
        courseAssistant1.setResponsibleName("name1");
        courseAssistant1.setResponsibleName2("name2");
        courseAssistant1.setResponsiblePhone("phone1");
        courseAssistant1.setResponsiblePhone2("phone2");
        lista.add(courseAssistant1);

        Mockito.when(courseAssistantRepository.findAllByCourseId("course1")).thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<org.uresti.pozarreal.dto.CourseAssistant> courseAssistant = courseAssistantService
                .findAllByCourse("course1");

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertNotNull(courseAssistant);
        Assertions.assertEquals("course1", courseAssistant.get(0).getCourseId());
        Assertions.assertEquals("hello", courseAssistant.get(0).getNotes());
        Assertions.assertEquals("email@email.com", courseAssistant.get(0).getEmail());
        Assertions.assertEquals(now, courseAssistant.get(0).getBirthDate());
        Assertions.assertEquals("123456789", courseAssistant.get(0).getPhone());
        Assertions.assertEquals("name", courseAssistant.get(0).getName());
        Assertions.assertEquals("name1", courseAssistant.get(0).getResponsibleName());
        Assertions.assertEquals("name2", courseAssistant.get(0).getResponsibleName2());
        Assertions.assertEquals("phone1", courseAssistant.get(0).getResponsiblePhone());
        Assertions.assertEquals("phone2", courseAssistant.get(0).getResponsiblePhone2());
    }

    @Test
    public void givenAnECourseAssistantWithCorrectId_whenGetCourseAssistant_thenIsDeleted() {
        // Given:
        CourseAssistantRepository courseAssistantRepository = Mockito.mock(CourseAssistantRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantServiceImpl courseAssistantService = new CourseAssistantServiceImpl(courseAssistantRepository);
        LoggedUser user = LoggedUser.builder().build();

        LocalDate now = LocalDate.now();
        List<CourseAssistant> lista = new LinkedList<>();

        CourseAssistant courseAssistant1 = new CourseAssistant();
        courseAssistant1.setId("id1");
        courseAssistant1.setCourseId("course1");
        courseAssistant1.setNotes("hello");
        courseAssistant1.setEmail("email@email.com");
        courseAssistant1.setBirthDate(now);
        courseAssistant1.setPhone("123456789");
        courseAssistant1.setName("name");
        courseAssistant1.setResponsibleName("name1");
        courseAssistant1.setResponsibleName2("name2");
        courseAssistant1.setResponsiblePhone("phone1");
        courseAssistant1.setResponsiblePhone2("phone2");

        Mockito.when(courseAssistantRepository.findAllByCourseId("course1")).thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        courseAssistantService.delete("id1");

        List<org.uresti.pozarreal.dto.CourseAssistant> courseAssistant = courseAssistantService.findAllByCourse("course1");

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertTrue(courseAssistant.isEmpty());
    }

    @Test
    public void givenAnECourseAssistantWithWrongId_whenGetCourseAssistant_thenIsNotDeleted() {
        // Given:
        CourseAssistantRepository courseAssistantRepository = Mockito.mock(CourseAssistantRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantServiceImpl courseAssistantService = new CourseAssistantServiceImpl(courseAssistantRepository);
        LoggedUser user = LoggedUser.builder().build();

        LocalDate now = LocalDate.now();
        List<CourseAssistant> lista = new LinkedList<>();

        CourseAssistant courseAssistant1 = new CourseAssistant();
        courseAssistant1.setId("id1");
        courseAssistant1.setCourseId("course1");
        courseAssistant1.setNotes("hello");
        courseAssistant1.setEmail("email@email.com");
        courseAssistant1.setBirthDate(now);
        courseAssistant1.setPhone("123456789");
        courseAssistant1.setName("name");
        courseAssistant1.setResponsibleName("name1");
        courseAssistant1.setResponsibleName2("name2");
        courseAssistant1.setResponsiblePhone("phone1");
        courseAssistant1.setResponsiblePhone2("phone2");
        lista.add(courseAssistant1);

        Mockito.doNothing().when(courseAssistantRepository).deleteById("id2");
        Mockito.when(courseAssistantRepository.findAllByCourseId("course4")).thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        courseAssistantService.delete("id2");

        List<org.uresti.pozarreal.dto.CourseAssistant> courseAssistant = courseAssistantService.findAllByCourse("course4");

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertEquals(1, courseAssistant.size());
    }
}