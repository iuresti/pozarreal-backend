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
import org.uresti.pozarreal.dto.User;
import org.uresti.pozarreal.service.UserService;
import org.uresti.pozarreal.testmode.config.TestAuthenticationProvider;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(UserController.class)
@ActiveProfiles("tests")
public class UserControllerTest {
    private final String BASE_URL = "/api";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SessionHelper sessionHelper;

    @MockBean
    private TestAuthenticationProvider testAuthenticationProvider;

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    public void givenAPrincipal_whenBuildUserForEmail_thenBuildUser() throws Exception {
        // Given:
        String email = "example@example.com";

        User user = User.builder().build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(sessionHelper.getEmailForLoggedUser(principal)).thenReturn(email);
        Mockito.when(userService.buildUserForEmail(email)).thenReturn(user);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/" + "loggedUser")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(userService).buildUserForEmail(email);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAUserWithUserManagerRole_whenGetAllUsers_thenListOfUsersIsReturned() throws Exception {
        // Given:
        List<User> users = new LinkedList<>();

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/" + "users")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(userService).getAllUsers();
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAUUserWithUserManagerRole_whenGetAllRoles_thenListOfAvailableRolesIsReturned() throws Exception {
        // Given:
        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + "/" + "users/roles")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isNotNull();
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAUserId_whenRemoveRole_thenUserRole() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();
        String role = "ROLE_ADMIN";
        String[] roles = {"ROLE_USER_MANAGER", "ROLE_RESIDENT"};

        Mockito.when(userService.removeRole(userId, role)).thenReturn(List.of(roles));

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/users/" + userId + "/roles/" + role)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(userService).removeRole(userId, role);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAUserId_whenAddRole_thenRoleIsAddedToListOfRolesByUser() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();
        String role = "ROLE_ADMIN";
        String[] roles = {"ROLE_USER_MANAGER", "ROLE_RESIDENT", "ROLE_ADMIN"};

        Mockito.when(userService.addRole(userId, role)).thenReturn(List.of(roles));

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL + "/users/" + userId + "/roles/" + role)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(userService).addRole(userId, role);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "RESIDENT")
    public void givenAnNewName_whenUpdateName_thenNameIsChangedByUser() throws Exception {
        // Given:
        String email = "example@example.com";
        String name = "new name";

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        User user = User.builder()
                .name(name)
                .id("id")
                .build();

        Mockito.when(userService.updateName(name, email)).thenReturn(user);
        Mockito.when(sessionHelper.getEmailForLoggedUser(principal)).thenReturn(email);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.patch(BASE_URL + "/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(name))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(userService).updateName(name, email);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER_MANAGER")
    public void givenAUserId_whenUpdateStatus_thenStatusIsUpdated() throws Exception {
        // Given:
        String userId = UUID.randomUUID().toString();
        Boolean status = false;

        User user = User.builder()
                .status(false)
                .id(userId)
                .name("name")
                .build();

        Mockito.when(userService.updateStatus(userId, status)).thenReturn(user);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.patch(BASE_URL + "/users/status/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.valueOf(status)))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verify(userService).updateStatus(userId, status);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "RESIDENT")
    public void given_whenLogout_whenLogout() throws Exception {
        // Given:
        LoggedUser loggedUser = LoggedUser.builder()
                .claims(Collections.singletonMap("userId", "id"))
                .name("name")
                .build();

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        Mockito.when(sessionHelper.getLoggedUser(principal)).thenReturn(loggedUser);

        // When:
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/logout/"))
                .andReturn().getResponse();

        // Then:
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Mockito.verifyNoMoreInteractions(userService);
    }
}