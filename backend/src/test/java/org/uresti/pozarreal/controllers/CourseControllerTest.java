package org.uresti.pozarreal.controllers;

import org.assertj.core.api.Assertions;
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
import org.uresti.pozarreal.dto.Course;
import org.uresti.pozarreal.service.CoursesService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.LinkedList;
import java.util.List;

@WebMvcTest(CourseController.class)
@ActiveProfiles("tests")
public class CourseControllerTest {
    private final String BASE_URL = "/api/courses";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoursesService coursesService;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "SCHOOL_MANAGER")
    public void given_whenGetCourses_thenListOfCoursesIsReturned() throws Exception {
        // Given:
        List<Course> courses = new LinkedList<>();

        Mockito.when(coursesService.findAll()).thenReturn(courses);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(coursesService).findAll();
        Mockito.verifyNoMoreInteractions(coursesService);
    }
}
