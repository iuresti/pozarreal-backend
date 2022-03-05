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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.StreetInfo;
import org.uresti.pozarreal.service.StreetsService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.UUID;

@WebMvcTest(StreetInfoController.class)
@ActiveProfiles("tests")
public class StreetInfoControllerTest {

    private final String BASE_URL = "/api/streetInfo";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StreetsService streetsService;

    @MockBean
    private SessionHelper sessionHelper;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAStreetIdAndStartYear_whenGetStreetInfo_thenFindStreetInfoAndIsReturned() throws Exception {
        // Given:
        String streetId = UUID.randomUUID().toString();

        LoggedUser loggedUser = LoggedUser.builder()
                .name("name")
                .build();

        StreetInfo streetInfo = StreetInfo.builder().build();

        Integer startYear = 2000;

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(streetsService.getStreetInfo(streetId, loggedUser, startYear)).thenReturn(streetInfo);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/" + streetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("year", String.valueOf(startYear)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(streetsService).getStreetInfo(streetId, loggedUser, startYear);
        Mockito.verifyNoMoreInteractions(streetsService);
    }
}