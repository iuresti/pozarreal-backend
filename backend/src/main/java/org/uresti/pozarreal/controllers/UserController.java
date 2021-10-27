package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.User;
import org.uresti.pozarreal.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController {

    private final List<String> availableRoles;

    private final UserService userService;

    private final SessionHelper sessionHelper;

    public UserController(UserService userService,
                          SessionHelper sessionHelper) {
        this.userService = userService;
        this.sessionHelper = sessionHelper;
        availableRoles = Arrays.stream(Role.values()).map(Enum::name).collect(Collectors.toList());
    }

    @GetMapping("/api/loggedUser")
    public User getLoginInfo(Principal principal) {
        String email = sessionHelper.getEmailForLoggedUser(principal);

        return userService.buildUserForEmail(email);
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/users/roles")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public List<String> getAllRoles() {
        return availableRoles;
    }

    @DeleteMapping("/api/users/{userId}/roles/{role}")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public List<String> removeRole(@PathVariable String userId, @PathVariable String role) {
        return userService.removeRole(userId, role);
    }

    @PostMapping("/api/users/{userId}/roles/{role}")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public List<String> addRole(@PathVariable String userId, @PathVariable String role) {
        return userService.addRole(userId, role);
    }

    @PutMapping("/api/users/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER')")
    public User updateUser(@PathVariable String userId, @RequestBody User user, Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            String email = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");

            User userLogged = userService.buildUserForEmail(email);

            if (userLogged.getId().equals(userId) && userId.equals(user.getId())) {
                return userService.save(user);
            }
        }
        return null;
    }

    @GetMapping("logout")
    public void logout(Principal principal) {

        LoggedUser user = sessionHelper.getLoggedUser(principal);

        log.info("Logging out user: {}", user.getName());
    }
}
