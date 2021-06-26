package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.uresti.pozarreal.dto.User;
import org.uresti.pozarreal.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    private final List<String> availableRoles;

    private final UserService userService;

    private final SessionHelper sessionHelper;

    public UserController(OAuth2AuthorizedClientService authorizedClientService,
                          UserService userService,
                          SessionHelper sessionHelper) {
        this.authorizedClientService = authorizedClientService;
        this.userService = userService;
        this.sessionHelper = sessionHelper;
        availableRoles = Arrays.asList("ROLE_ADMIN", "ROLE_RESIDENT");
    }

//    @GetMapping("/getUserInfo")
//    public Map getLoginInfo(Principal principal) {
//        OAuth2AuthorizedClient client = authorizedClientService
//                .loadAuthorizedClient(
//                        authentication.getAuthorizedClientRegistrationId(),
//                        authentication.getName());
//
//        String userInfoEndpointUri = client.getClientRegistration()
//                .getProviderDetails().getUserInfoEndpoint().getUri();
//
//        if (StringUtils.hasLength(userInfoEndpointUri)) {
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
//                    .getTokenValue());
//            HttpEntity entity = new HttpEntity("", headers);
//            ResponseEntity<Map> response = restTemplate
//                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
//            Map userAttributes = response.getBody();
//
//            return userAttributes;
//        }
//
//        return null;
//    }

    @GetMapping("/api/loggedUser")
    public User getLoginInfo(Principal principal) {
        String email = sessionHelper.getEmailForLoggedUser(principal);

        return userService.buildUserForEmail(email);

    }

    @GetMapping("/api/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/users/roles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<String> getAllRoles() {
        return availableRoles;
    }

    @DeleteMapping("/api/users/{userId}/roles/{role}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<String> removeRole(@PathVariable String userId, @PathVariable String role) {
        return userService.removeRole(userId, role);
    }

    @PostMapping("/api/users/{userId}/roles/{role}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<String> addRole(@PathVariable String userId, @PathVariable String role) {
        return userService.addRole(userId, role);
    }

    @PutMapping("/api/users/{userId}")
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
}
