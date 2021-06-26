package org.uresti.pozarreal.controllers;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.uresti.pozarreal.dto.LoggedUser;

import java.security.Principal;

@Component
public class SessionHelper {

    public String getEmailForLoggedUser(Principal principal){
        LoggedUser loggedUser = (LoggedUser) ((OAuth2AuthenticationToken) principal).getPrincipal();

        return loggedUser.getEmail();
    }

    public String getUserIdForLoggedUser(Principal principal){
        LoggedUser loggedUser = (LoggedUser) ((OAuth2AuthenticationToken) principal).getPrincipal();

        return loggedUser.getUserId();
    }

    public LoggedUser getLoggedUser(Principal principal) {
        return (LoggedUser) ((OAuth2AuthenticationToken) principal).getPrincipal();
    }
}
