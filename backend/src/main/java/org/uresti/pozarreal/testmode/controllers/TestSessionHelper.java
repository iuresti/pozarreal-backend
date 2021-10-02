package org.uresti.pozarreal.testmode.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.LoggedUser;

import java.security.Principal;

@Component
@Profile("tests")
public class TestSessionHelper implements SessionHelper {

    public String getEmailForLoggedUser(Principal principal) {
        LoggedUser loggedUser = (LoggedUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return loggedUser.getEmail();
    }

    public String getUserIdForLoggedUser(Principal principal) {
        LoggedUser loggedUser = (LoggedUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return loggedUser.getUserId();
    }

    public LoggedUser getLoggedUser(Principal principal) {
        return (LoggedUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

    public boolean hasRole(LoggedUser user, Role role) {
        return user.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }
}
