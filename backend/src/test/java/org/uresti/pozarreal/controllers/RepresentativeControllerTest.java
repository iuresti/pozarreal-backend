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
import org.uresti.pozarreal.dto.Representative;
import org.uresti.pozarreal.service.RepresentativesService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.UUID;

@WebMvcTest(RepresentativeController.class)
@ActiveProfiles("tests")
public class RepresentativeControllerTest {

    private final String BASE_URL = "/api/representatives";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepresentativesService representativesService;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenANewStreetForAnUser_whenSaveStreet_thenChangeStreetForThatUser() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();
        String streetId = UUID.randomUUID().toString();

        Representative representative = Representative.builder()
                .userId(userId)
                .street(streetId)
                .houseNumber("100")
                .build();

        Mockito.when(representativesService.saveStreet(userId, streetId)).thenReturn(representative);

        // When:
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/" + userId + "/street/" + streetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(representative)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(representativesService).saveStreet(userId, streetId);
        Mockito.verifyNoMoreInteractions(representativesService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAnUserId_whenDelete_thenDeleteThatUserRepresentative() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();

        // When:
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + userId))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(representativesService).delete(userId);
        Mockito.verifyNoMoreInteractions(representativesService);
    }
}