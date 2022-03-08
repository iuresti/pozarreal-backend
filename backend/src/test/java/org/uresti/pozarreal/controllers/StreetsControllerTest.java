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
import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.service.HousesService;
import org.uresti.pozarreal.service.StreetsService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.*;

@WebMvcTest(StreetsController.class)
@ActiveProfiles("tests")
public class StreetsControllerTest {
    private final String BASE_URL = "/api/streets";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StreetsService streetsService;

    @MockBean
    private HousesService housesService;

    @MockBean
    private SessionHelper sessionHelper;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPrincipal_whenGetStreets_thenFindAndIsReturnedAListOfStreet() throws Exception {
        // Given:
        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        List<Street> streets = new LinkedList<>();
        Mockito.when(streetsService.getStreets(loggedUser)).thenReturn(streets);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(streetsService).getStreets(loggedUser);
        Mockito.verifyNoMoreInteractions(streetsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAStreetId_whenGetHousesByStreet_thenFindStreetsByStreetIdAndAListOfHousesIsReturned() throws Exception {
        // Given:
        String streetId = UUID.randomUUID().toString();
        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        List<House> houses = new LinkedList<>();

        Mockito.when(housesService.getHousesByStreet(streetId, loggedUser)).thenReturn(houses);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/" + streetId + "/houses")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).getHousesByStreet(streetId, loggedUser);
        Mockito.verifyNoMoreInteractions(streetsService);
    }
}
