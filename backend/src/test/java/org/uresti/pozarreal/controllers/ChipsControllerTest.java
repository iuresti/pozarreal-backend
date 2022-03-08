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
import org.uresti.pozarreal.dto.Chip;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.service.ChipsService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(ChipsController.class)
@ActiveProfiles("tests")
public class ChipsControllerTest {
    private final String BASE_URL = "/api/chips";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChipsService chipsService;

    @MockBean
    private SessionHelper sessionHelper;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAHouseId_whenGetChipsByHouse_thenAChipsListIsReturned() throws Exception {
        // Given:
        String houseId = UUID.randomUUID().toString();

        List<Chip> chips = new LinkedList<>();
        Mockito.when(chipsService.getChipsByHouse(houseId)).thenReturn(chips);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/house/" + houseId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(chipsService).getChipsByHouse(houseId);
        Mockito.verifyNoMoreInteractions(chipsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAChipId_whebActivateChip_thenChipIsActivated() throws Exception {
        // Given:
        String chipId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Chip chip = Chip.builder()
                .id("id")
                .code("code")
                .house("house")
                .notes("note")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .name("name")
                .claims(Collections.singletonMap("userId", userId))
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(chipsService.activateChip(chipId)).thenReturn(chip);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.patch(BASE_URL + "/activate/" + chipId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(chipsService).activateChip(chipId);
        Mockito.verifyNoMoreInteractions(chipsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAChipId_whenDeactivateChip_thenChipIsDeactivated() throws Exception {
        // Given:
        String chipId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Chip chip = Chip.builder()
                .id("id")
                .code("code")
                .house("house")
                .notes("note")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .name("name")
                .claims(Collections.singletonMap("userId", userId))
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(chipsService.deactivateChip(chipId)).thenReturn(chip);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.patch(BASE_URL + "/deactivate/" + chipId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(chipsService).deactivateChip(chipId);
        Mockito.verifyNoMoreInteractions(chipsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAChip_whenAddChipp_thenChipsIsAdded() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();

        Chip chip = Chip.builder()
                .id("id")
                .code("code")
                .house("house")
                .notes("note")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .name("name")
                .claims(Collections.singletonMap("userId", userId))
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(chipsService.addChip(chip)).thenReturn(chip);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(chip)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(chipsService).addChip(chip);
        Mockito.verifyNoMoreInteractions(chipsService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAChipId_whenDeleteChip_thenChipIsDeleted() throws Exception {
        // Given:
        String chipId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        Chip chip = Chip.builder()
                .id("id")
                .code("code")
                .house("house")
                .notes("note")
                .build();

        LoggedUser loggedUser = LoggedUser.builder()
                .name("name")
                .claims(Collections.singletonMap("userId", userId))
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(chipsService.deleteChip(chipId)).thenReturn(chip);
        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/" + chipId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(chipsService).deleteChip(chipId);
        Mockito.verifyNoMoreInteractions(chipsService);
    }
}
