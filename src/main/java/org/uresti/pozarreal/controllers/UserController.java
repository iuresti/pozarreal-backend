package org.uresti.pozarreal.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.uresti.pozarreal.dto.User;
import org.uresti.pozarreal.service.UserService;

@RestController
public class UserController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    private final List<String> availableRoles;

    private final UserService userService;

    public UserController(OAuth2AuthorizedClientService authorizedClientService, UserService userService) {
        this.authorizedClientService = authorizedClientService;
        this.userService = userService;
        availableRoles = Arrays.asList("ROLE_ADMIN", "ROLE_RESIDENT");
    }

    @GetMapping("/getUserInfo")
    public Map getLoginInfo(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        if (StringUtils.hasLength(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();

            return userAttributes;
        }

        return null;
    }

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/users/roles")
    public List<String> getAllRoles() {
        return availableRoles;
    }

    @DeleteMapping("/api/users/{userId}/roles/{role}")
    public List<String> removeRole(@PathVariable String userId, @PathVariable String role) {
        return userService.removeRole(userId, role);
    }

    @PostMapping("/api/users/{userId}/roles/{role}")
    public List<String> addRole(@PathVariable String userId, @PathVariable String role) {
        return userService.addRole(userId, role);
    }
}
