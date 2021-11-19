package org.uresti.pozarreal.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.CoursePayment;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.repository.CourseAssistantPaymentRepository;
import org.uresti.pozarreal.service.mappers.CourseAssistantPaymentMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class CourseAssistantPaymentServiceImplTests {
    @Test
    public void givenAnEmptyCoursePayment_whenGetCoursePayment_thenEmptyListIsReturned() {
        // Given:
        CourseAssistantPaymentRepository courseAssistantPaymentRepository = Mockito.mock(CourseAssistantPaymentRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantPaymentServiceImpl courseAssistantPaymentService = new CourseAssistantPaymentServiceImpl(courseAssistantPaymentRepository);
        LoggedUser user = LoggedUser.builder().build();
        List<org.uresti.pozarreal.model.CoursePayment> lista = new LinkedList<>();

        Mockito.when(courseAssistantPaymentRepository.findAllByCourseAssistantIdOrderByPaymentDateDesc(null)).thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<CoursePayment> courses = courseAssistantPaymentService.findAllByCourseAssistant(null);

        // Then:
        Assertions.assertTrue(courses.isEmpty());
    }

    @Test
    public void givenAnCoursePaymentWithTwoElements_whenGetCoursesPayment_thenListWithTwoElementsIsReturned() {
        // Given:
        CourseAssistantPaymentRepository courseAssistantPaymentRepository = Mockito
                .mock(CourseAssistantPaymentRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantPaymentServiceImpl courseAssistantPaymentService = new
                CourseAssistantPaymentServiceImpl(courseAssistantPaymentRepository);
        LoggedUser user = LoggedUser.builder().build();
        List<org.uresti.pozarreal.model.CoursePayment> lista = new LinkedList<>();
        LocalDate now = LocalDate.now();

        org.uresti.pozarreal.model.CoursePayment coursePayment1 = new org.uresti.pozarreal.model.CoursePayment();
        coursePayment1.setId("id1");
        coursePayment1.setPaymentDate(now);
        coursePayment1.setUserId("User 1");
        coursePayment1.setAmount(new BigDecimal(50));
        coursePayment1.setCourseAssistantId("abc");
        coursePayment1.setReceiptNumber("123");
        coursePayment1.setNotes("hello");
        coursePayment1.setConcept("Concept 1");
        lista.add(coursePayment1);

        org.uresti.pozarreal.model.CoursePayment coursePayment2 = new org.uresti.pozarreal.model.CoursePayment();
        coursePayment2.setId("id2");
        coursePayment2.setCourseAssistantId("abc");
        coursePayment2.setAmount(new BigDecimal(50));
        coursePayment2.setUserId("User 2");
        coursePayment2.setReceiptNumber("123");
        coursePayment2.setNotes("hello");
        coursePayment2.setConcept("Concept 2");
        coursePayment2.setPaymentDate(now);
        lista.add(coursePayment2);

        Mockito.when(courseAssistantPaymentRepository.findAllByCourseAssistantIdOrderByPaymentDateDesc("abc"))
                .thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<CoursePayment> courseAssistant = courseAssistantPaymentService
                .findAllByCourseAssistant("abc");

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertEquals(2, courseAssistant.size());
        Assertions.assertEquals("id1", courseAssistant.get(0).getId());
        Assertions.assertEquals(now, courseAssistant.get(0).getPaymentDate());
        Assertions.assertEquals("User 1", courseAssistant.get(0).getUserId());
        Assertions.assertEquals(new BigDecimal(50), courseAssistant.get(0).getAmount());
        Assertions.assertEquals("abc", courseAssistant.get(0).getCourseAssistantId());
        Assertions.assertEquals("123", courseAssistant.get(0).getReceiptNumber());
        Assertions.assertEquals("hello", courseAssistant.get(0).getNotes());
        Assertions.assertEquals("Concept 1", courseAssistant.get(0).getConcept());
        Assertions.assertEquals("id2", courseAssistant.get(1).getId());
        Assertions.assertEquals(now, courseAssistant.get(1).getPaymentDate());
        Assertions.assertEquals("User 2", courseAssistant.get(1).getUserId());
        Assertions.assertEquals(new BigDecimal(50), courseAssistant.get(1).getAmount());
        Assertions.assertEquals("abc", courseAssistant.get(1).getCourseAssistantId());
        Assertions.assertEquals("123", courseAssistant.get(1).getReceiptNumber());
        Assertions.assertEquals("hello", courseAssistant.get(1).getNotes());
        Assertions.assertEquals("Concept 2", courseAssistant.get(1).getConcept());
    }

    @Test
    public void givenAnCoursePayment_whenGetCoursePayment_thenIsSaved() {
        // Given:
        CourseAssistantPaymentRepository courseAssistantPaymentRepository = Mockito
                .mock(CourseAssistantPaymentRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantPaymentServiceImpl courseAssistantPaymentService = new
                CourseAssistantPaymentServiceImpl(courseAssistantPaymentRepository);
        LoggedUser user = LoggedUser.builder().build();
        LocalDate now = LocalDate.now();

        List<org.uresti.pozarreal.model.CoursePayment> lista = new LinkedList<>();

        org.uresti.pozarreal.model.CoursePayment coursePayment = new org.uresti.pozarreal.model.CoursePayment();
        coursePayment.setId("id1");
        coursePayment.setPaymentDate(now);
        coursePayment.setUserId("User 1");
        coursePayment.setAmount(new BigDecimal(50));
        coursePayment.setCourseAssistantId("abc");
        coursePayment.setNotes("hello");
        coursePayment.setConcept("Concept 1");
        coursePayment.setReceiptNumber("123");

        lista.add(coursePayment);
        Mockito.when(courseAssistantPaymentRepository.save(coursePayment)).thenReturn(coursePayment);
        Mockito.when(courseAssistantPaymentRepository.findAllByCourseAssistantIdOrderByPaymentDateDesc("abc"))
                .thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        CoursePayment courseAssistant = courseAssistantPaymentService
                .save(CourseAssistantPaymentMapper.entityToDto(coursePayment));

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertNotNull(courseAssistant);
        Assertions.assertEquals("id1", courseAssistant.getId());
        Assertions.assertEquals(now, courseAssistant.getPaymentDate());
        Assertions.assertEquals("User 1", courseAssistant.getUserId());
        Assertions.assertEquals(new BigDecimal(50), courseAssistant.getAmount());
        Assertions.assertEquals("abc", courseAssistant.getCourseAssistantId());
        Assertions.assertEquals("123", courseAssistant.getReceiptNumber());
        Assertions.assertEquals("hello", courseAssistant.getNotes());
        Assertions.assertEquals("Concept 1", courseAssistant.getConcept());
    }

    @Test
    public void givenAnCoursePayment_whenGetCoursePaymentIdIsNull_thenIsSavedAndSetId() {
        // Given:
        CourseAssistantPaymentRepository courseAssistantPaymentRepository = Mockito
                .mock(CourseAssistantPaymentRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantPaymentServiceImpl courseAssistantPaymentService = new
                CourseAssistantPaymentServiceImpl(courseAssistantPaymentRepository);
        LoggedUser user = LoggedUser.builder().build();
        List<org.uresti.pozarreal.model.CoursePayment> lista = new LinkedList<>();
        LocalDate now = LocalDate.now();

        org.uresti.pozarreal.model.CoursePayment coursePayment1 = new org.uresti.pozarreal.model.CoursePayment();
        coursePayment1.setUserId("User 1");
        coursePayment1.setPaymentDate(now);
        coursePayment1.setAmount(new BigDecimal(50));
        coursePayment1.setCourseAssistantId("abc");
        coursePayment1.setReceiptNumber("123");
        coursePayment1.setNotes("hello");
        coursePayment1.setConcept("Concept 1");
        lista.add(coursePayment1);

        Mockito.when(courseAssistantPaymentRepository.findAllByCourseAssistantIdOrderByPaymentDateDesc("abc"))
                .thenReturn(lista);
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        List<CoursePayment> courseAssistant = courseAssistantPaymentService
                .findAllByCourseAssistant("abc");

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertEquals(1, courseAssistant.size());
        Assertions.assertEquals(now, courseAssistant.get(0).getPaymentDate());
        Assertions.assertEquals("User 1", courseAssistant.get(0).getUserId());
        Assertions.assertEquals(new BigDecimal(50), courseAssistant.get(0).getAmount());
        Assertions.assertEquals("abc", courseAssistant.get(0).getCourseAssistantId());
        Assertions.assertEquals("123", courseAssistant.get(0).getReceiptNumber());
        Assertions.assertEquals("hello", courseAssistant.get(0).getNotes());
        Assertions.assertEquals("Concept 1", courseAssistant.get(0).getConcept());
    }

    @Test
    public void givenAnCoursePayment_whenGetCoursePaymentIdCorrect_thenIsDeleted() {
        // Given:
        CourseAssistantPaymentRepository courseAssistantPaymentRepository = Mockito
                .mock(CourseAssistantPaymentRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantPaymentServiceImpl courseAssistantPaymentService = new
                CourseAssistantPaymentServiceImpl(courseAssistantPaymentRepository);
        LoggedUser user = LoggedUser.builder().build();
        LocalDate now = LocalDate.now();

        List<org.uresti.pozarreal.model.CoursePayment> lista = new LinkedList<>();

        org.uresti.pozarreal.model.CoursePayment coursePayment = new org.uresti.pozarreal.model.CoursePayment();
        coursePayment.setId("id1");
        coursePayment.setPaymentDate(now);
        coursePayment.setUserId("User 1");
        coursePayment.setAmount(new BigDecimal(50));
        coursePayment.setCourseAssistantId("abc");
        coursePayment.setNotes("hello");
        coursePayment.setConcept("Concept 1");
        coursePayment.setReceiptNumber("123");

        lista.add(coursePayment);
        Mockito.when(courseAssistantPaymentRepository.save(coursePayment)).thenReturn(coursePayment);
        Mockito.when(courseAssistantPaymentRepository.findAllByCourseAssistantIdOrderByPaymentDateDesc("abc"))
                .thenReturn(lista);
        courseAssistantPaymentRepository.deleteById("id1");
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        courseAssistantPaymentService.delete("id1");

        CoursePayment courseAssistant = courseAssistantPaymentService
                .save(CourseAssistantPaymentMapper.entityToDto(coursePayment));

        List<CoursePayment> coursePaymentList = courseAssistantPaymentService.findAllByCourseAssistant("id1");

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertEquals(0, coursePaymentList.size());
        Assertions.assertNotNull(courseAssistant);
    }

    @Test
    public void givenAnCoursePayment_whenGetCoursePaymentIdWrong_thenIsNotDeleted() {
        // Given:
        CourseAssistantPaymentRepository courseAssistantPaymentRepository = Mockito
                .mock(CourseAssistantPaymentRepository.class);
        SessionHelper mockSessionHelper = Mockito.mock(SessionHelper.class);
        CourseAssistantPaymentServiceImpl courseAssistantPaymentService = new
                CourseAssistantPaymentServiceImpl(courseAssistantPaymentRepository);
        LoggedUser user = LoggedUser.builder().build();
        LocalDate now = LocalDate.now();

        List<org.uresti.pozarreal.model.CoursePayment> lista = new LinkedList<>();

        org.uresti.pozarreal.model.CoursePayment coursePayment = new org.uresti.pozarreal.model.CoursePayment();
        coursePayment.setId("id1");
        coursePayment.setPaymentDate(now);
        coursePayment.setUserId("User 1");
        coursePayment.setAmount(new BigDecimal(50));
        coursePayment.setCourseAssistantId("abc");
        coursePayment.setNotes("hello");
        coursePayment.setConcept("Concept 1");
        coursePayment.setReceiptNumber("123");

        lista.add(coursePayment);
        Mockito.when(courseAssistantPaymentRepository.save(coursePayment)).thenReturn(coursePayment);
        Mockito.when(courseAssistantPaymentRepository.findAllByCourseAssistantIdOrderByPaymentDateDesc("abc"))
                .thenReturn(lista);
        courseAssistantPaymentRepository.deleteById("id1");
        Mockito.when(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER)).thenReturn(true);

        // When:
        courseAssistantPaymentService.delete("123456");

        CoursePayment courseAssistant = courseAssistantPaymentService
                .save(CourseAssistantPaymentMapper.entityToDto(coursePayment));

        List<CoursePayment> coursePaymentList = courseAssistantPaymentService.findAllByCourseAssistant("abc");

        // Then:
        Assertions.assertTrue(mockSessionHelper.hasRole(user, Role.ROLE_SCHOOL_MANAGER));
        Assertions.assertEquals(1, coursePaymentList.size());
        Assertions.assertNotNull(courseAssistant);
    }
}