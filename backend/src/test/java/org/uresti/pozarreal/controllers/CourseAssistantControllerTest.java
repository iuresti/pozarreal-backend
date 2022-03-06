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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.uresti.pozarreal.config.controller.LocalDateAdapter;
import org.uresti.pozarreal.dto.CourseAssistant;
import org.uresti.pozarreal.service.CourseAssistantService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@WebMvcTest(CourseAssistantController.class)
@ActiveProfiles("tests")
public class CourseAssistantControllerTest {
    private final String BASE_URL = "/api/course-assistants";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseAssistantService courseAssistantService;

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
    public void givenACourseId_whenFindAllByCourse_thenAListOfCourseAssistantIsReturned() throws Exception {
        // Given:
        String courseId = UUID.randomUUID().toString();

        List<CourseAssistant> courseAssistants = new LinkedList<>();

        Mockito.when(courseAssistantService.findAllByCourse(courseId)).thenReturn(courseAssistants);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/course/" + courseId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(courseAssistantService).findAllByCourse(courseId);
        Mockito.verifyNoMoreInteractions(courseAssistantService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "SCHOOL_MANAGER")
    public void givenACourseAssistant_whenSave_thenCourseAssistantIsSaved() throws Exception {
        // Given:
        CourseAssistant courseAssistant = CourseAssistant.builder()
                .courseId("courseId")
                .id("id")
                .age(17)
                .email("example@example.com")
                .name("name")
                .build();

        Mockito.when(courseAssistantService.save(courseAssistant)).thenReturn(courseAssistant);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(courseAssistant)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(courseAssistantService).save(courseAssistant);
        Mockito.verifyNoMoreInteractions(courseAssistantService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "SCHOOL_MANAGER")
    public void givenACourseAssistant_whenUpdate_thenCourseAssistantIsUpdated() throws Exception {
        // Given:
        CourseAssistant courseAssistant = CourseAssistant.builder()
                .courseId("courseId")
                .id("id")
                .age(17)
                .email("example@example.com")
                .name("name")
                .build();

        Mockito.when(courseAssistantService.save(courseAssistant)).thenReturn(courseAssistant);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(courseAssistant)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(courseAssistantService).save(courseAssistant);
        Mockito.verifyNoMoreInteractions(courseAssistantService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "SCHOOL_MANAGER")
    public void givenACourseAssistantId_whenDelete_thenCourseAssistantIsDeleted() throws Exception {
        // Given:
        String courseAssistantId = UUID.randomUUID().toString();

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/" + courseAssistantId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(courseAssistantService).delete(courseAssistantId);
        Mockito.verifyNoMoreInteractions(courseAssistantService);
    }
}
