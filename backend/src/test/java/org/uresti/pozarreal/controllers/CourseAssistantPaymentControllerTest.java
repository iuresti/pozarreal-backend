package org.uresti.pozarreal.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.uresti.pozarreal.config.controller.LocalDateAdapter;
import org.uresti.pozarreal.dto.CoursePayment;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.service.CourseAssistantPaymentService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(CourseAssistantPaymentController.class)
@ActiveProfiles("tests")
public class CourseAssistantPaymentControllerTest {
    private final String BASE_URL = "/api/course-assistants-payment";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseAssistantPaymentService courseAssistantPaymentService;

    @MockBean
    private SessionHelper sessionHelper;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    private Gson gson;

    @BeforeEach
    public void setup() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "SCHOOL_MANAGER")
    public void givenACourseAssistantId_whenFindAllByCourseAssistant_thenListOfCoursePaymentIsReturned() throws Exception {
        // Given:
        String courseAssistantId = UUID.randomUUID().toString();

        List<CoursePayment> coursePayments = new LinkedList<>();

        Mockito.when(courseAssistantPaymentService.findAllByCourseAssistant(courseAssistantId)).thenReturn(coursePayments);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/assistant/" + courseAssistantId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(courseAssistantPaymentService).findAllByCourseAssistant(courseAssistantId);
        Mockito.verifyNoMoreInteractions(courseAssistantPaymentService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "SCHOOL_MANAGER")
    public void givenACoursePayment_whenSave_thenCoursePaymentIsSaved() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();

        CoursePayment coursePayment = CoursePayment.builder()
                .userId(userId)
                .courseAssistantId("id")
                .amount(BigDecimal.valueOf(100))
                .concept("concept")
                .notes("notes")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", userId))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(courseAssistantPaymentService.save(coursePayment)).thenReturn(coursePayment);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(coursePayment)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(courseAssistantPaymentService).save(coursePayment);

        Mockito.verifyNoMoreInteractions(courseAssistantPaymentService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "SCHOOL_MANAGER")
    public void givenACoursePayment_whenUpdate_thenCoursePaymentIsUpdated() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();

        CoursePayment coursePayment = CoursePayment.builder()
                .userId(userId)
                .courseAssistantId("id")
                .amount(BigDecimal.valueOf(100))
                .concept("concept")
                .notes("notes")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", userId))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(courseAssistantPaymentService.save(coursePayment)).thenReturn(coursePayment);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(coursePayment)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(courseAssistantPaymentService).save(coursePayment);
        Mockito.verifyNoMoreInteractions(courseAssistantPaymentService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "SCHOOL_MANAGER")
    public void givenACoursePaymentId_whenDelete_thenCoursePaymentIsDeleted() throws Exception {
        // Given:
        String courseAssistantPaymentId = UUID.randomUUID().toString();

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/" + courseAssistantPaymentId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(courseAssistantPaymentService).delete(courseAssistantPaymentId);
        Mockito.verifyNoMoreInteractions(courseAssistantPaymentService);
    }

}