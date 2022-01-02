package org.uresti.pozarreal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public List<Notification> getNotifications(Principal principal) {
        LoggedUser loggedUser = sessionHelper.getLoggedUser(principal);
        return notificationsService.getNotifications(loggedUser.getUserId());
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public void saveNotification(@RequestBody String message) {
        notificationsService.saveNotification(message);
    }

    @PatchMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public void readNotification(@PathVariable String notificationId) {
        notificationsService.readNotification(notificationId);
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REPRESENTATIVE')")
    public void deleteNotification(@PathVariable String notificationId) {
        notificationsService.removeNotification(notificationId);
    }
}
