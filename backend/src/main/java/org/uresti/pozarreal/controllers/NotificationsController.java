package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.Notification;
import org.uresti.pozarreal.service.NotificationsService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationsController {

    private final NotificationsService notificationsService;
    private final SessionHelper sessionHelper;

    public NotificationsController(NotificationsService notificationsService, SessionHelper sessionHelper) {
        this.notificationsService = notificationsService;
        this.sessionHelper = sessionHelper;
    }

    @GetMapping()
    public List<Notification> getNotifications(Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);

        log.info("Getting notifications by user: {} - {}", loggedUser.getName(), loggedUser.getUserId());

        return notificationsService.getNotifications(loggedUser.getUserId());
    }
}
