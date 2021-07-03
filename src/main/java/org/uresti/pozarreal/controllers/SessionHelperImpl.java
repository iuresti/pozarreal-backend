package org.uresti.pozarreal.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.LoggedUser;

import java.security.Principal;

@Component
@Profile("!tests")
public class SessionHelperImpl implements SessionHelper {

    @Override
    public String getEmailForLoggedUser(Principal principal) {
        LoggedUser loggedUser = (LoggedUser) ((OAuth2AuthenticationToken) principal).getPrincipal();

        return loggedUser.getEmail();
    }

    @Override
    public String getUserIdForLoggedUser(Principal principal) {
        LoggedUser loggedUser = (LoggedUser) ((OAuth2AuthenticationToken) principal).getPrincipal();

        return loggedUser.getUserId();
    }

    @Override
    public LoggedUser getLoggedUser(Principal principal) {
        return (LoggedUser) ((OAuth2AuthenticationToken) principal).getPrincipal();
    }

    @Override
    public boolean hasRole(LoggedUser user, Role role) {
        return user.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }
}
