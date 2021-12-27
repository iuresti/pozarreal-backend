package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.Notification;

public class NotificationMapper {

    public static Notification entityToDto(org.uresti.pozarreal.model.Notification notification) {
        return Notification.builder()
                .message(notification.getMessage())
                .seen(notification.isSeen())
                .timestamp(notification.getTimestamp())
                .build();
    }

    public static org.uresti.pozarreal.model.Notification dtoToEntity(Notification notification) {
        return org.uresti.pozarreal.model.Notification.builder()
                .message(notification.getMessage())
                .seen(notification.isSeen())
                .timestamp(notification.getTimestamp())
                .build();
    }
}
