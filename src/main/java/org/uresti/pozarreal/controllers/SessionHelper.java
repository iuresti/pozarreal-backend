package org.uresti.pozarreal.controllers;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class SessionHelper {

    public String getEmailForLoggedUser(Principal principal){
        if(principal instanceof OAuth2AuthenticationToken) {
            return ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");
        }

        return null;
    }

}
