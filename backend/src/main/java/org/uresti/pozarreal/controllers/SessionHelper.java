package org.uresti.pozarreal.controllers;

import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.dto.LoggedUser;

import java.security.Principal;

public interface SessionHelper {
    String getEmailForLoggedUser(Principal principal);

    String getUserIdForLoggedUser(Principal principal);

    LoggedUser getLoggedUser(Principal principal);

    boolean hasRole(LoggedUser user, Role role);
}
