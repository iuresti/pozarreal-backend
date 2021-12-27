package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.Notification;

import java.util.List;

public interface NotificationsService {
    List<Notification> getNotifications(String userId);
}
