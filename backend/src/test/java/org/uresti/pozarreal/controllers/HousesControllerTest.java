package org.uresti.pozarreal.controllers;

import com.google.gson.Gson;
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
import org.uresti.pozarreal.dto.*;
import org.uresti.pozarreal.service.HousesService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(HousesController.class)
@ActiveProfiles("tests")
public class HousesControllerTest {

    private final String BASE_URL = "/api/house";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HousesService housesService;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenANewStreetForAnUser_whenSaveStreet_thenChangeStreetForThatUser() throws Exception {
        // Given:
        String houseId = UUID.randomUUID().toString();

        ToggleChipStatusRequest toggleChipStatusRequest = ToggleChipStatusRequest.builder()
                .houseId(houseId)
                .enable(false)
                .build();

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL + "/" + houseId + "/chips")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(toggleChipStatusRequest)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).toggleChipStatusRequest(toggleChipStatusRequest.getHouseId(), toggleChipStatusRequest.isEnable());
        Mockito.verifyNoMoreInteractions(housesService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAHouseId_whenGetHouseInfo_thenFindAndReturnHouseInfo() throws Exception {
        // Given:
        String houseId = UUID.randomUUID().toString();

        HouseInfo houseInfo = HouseInfo.builder().build();

        Mockito.when(housesService.getHouseInfo(houseId)).thenReturn(houseInfo);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/info/" + houseId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).getHouseInfo(houseId);
        Mockito.verifyNoMoreInteractions(housesService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenANotes_whenSaveNotes_thenNotesAreUpdatedOrSaved() throws Exception {
        // Given:
        String houseId = UUID.randomUUID().toString();
        String notes = "notes";

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.patch(BASE_URL + "/" + houseId + "/notes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(notes))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).saveNotes(houseId, notes);
        Mockito.verifyNoMoreInteractions(housesService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAUserId_whenGetHousesByUser_thenReturnAListWithHouses() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();

        List<HouseByUser> housesByUser = new LinkedList<>();

        Mockito.when(housesService.getHousesByUser(userId)).thenReturn(housesByUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/" + userId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).getHousesByUser(userId);
        Mockito.verifyNoMoreInteractions(housesService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "RESIDENT")
    public void givenUserIdGotByPrincipal_whenGetHousesByUser_thenReturnAListWithHouses() throws Exception {
        // Given:
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        List<HouseByUser> housesByUser = new LinkedList<>();

        Mockito.when(housesService.getHousesByUser(principal)).thenReturn(housesByUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).getHousesByUser(principal);
        Mockito.verifyNoMoreInteractions(housesService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "RESIDENT")
    public void given_AHouseId_when_GetPaymentsHouse_thenIsReturnedAListWithPaymentByConcept() throws Exception {
        // Given:
        String houseId = UUID.randomUUID().toString();

        ArrayList<PaymentByConcept> paymentByConcepts = new ArrayList<>();

        Mockito.when(housesService.getPaymentsHouse(houseId)).thenReturn(paymentByConcepts);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/payments/" + houseId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).getPaymentsHouse(houseId);
        Mockito.verifyNoMoreInteractions(housesService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAnId_whenDeleteHouseByUser_thenHouseIsDeleteForThatUser() throws Exception {
        // Given:
        String id = UUID.randomUUID().toString();

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/" + id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).deleteHouseByUser(id);
        Mockito.verifyNoMoreInteractions(housesService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAHouseByUser_whenSaveHouseByUser_thenSaveThatHouse() throws Exception {
        // Given:
        String id = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        HouseByUser houseByUser = HouseByUser.builder()
                .id(id)
                .userId(userId)
                .build();

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(houseByUser)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(housesService).saveHouseByUser(houseByUser);
        Mockito.verifyNoMoreInteractions(housesService);
    }
}
