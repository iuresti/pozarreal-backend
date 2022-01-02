package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.Notification;

import java.util.List;


public interface NotificationsService {
    List<Notification> getNotifications(String userId);

    void saveNotification(String message);

    void readNotification(String notificationId);

    void removeNotification(String notificationId);
}
