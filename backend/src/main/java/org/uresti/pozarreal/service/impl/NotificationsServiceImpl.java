package org.uresti.pozarreal.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.dto.Notification;
import org.uresti.pozarreal.repository.NotificationsRepository;
import org.uresti.pozarreal.service.NotificationsService;
import org.uresti.pozarreal.service.UserService;
import org.uresti.pozarreal.service.mappers.NotificationMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationsServiceImpl implements NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private final UserService userService;

    public NotificationsServiceImpl(NotificationsRepository notificationsRepository, UserService userService) {
        this.notificationsRepository = notificationsRepository;
        this.userService = userService;
    }

    @Override
    public List<Notification> getNotifications(String userId) {
        return notificationsRepository.findAllByUserIdOrderByTimestampDesc(userId)
                .stream().map(NotificationMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public void saveNotification(String message) {

        userService.getAllUsers().stream()
                .filter(user -> user.getRoles().contains(Role.ROLE_REPRESENTATIVE.name()) || user.getRoles().contains(Role.ROLE_ADMIN.name()))
                .forEach(user -> {
                    org.uresti.pozarreal.model.Notification notification = org.uresti.pozarreal.model.Notification.builder()
                            .id(UUID.randomUUID().toString())
                            .message(message)
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .userId(user.getId())
                            .build();

                    notificationsRepository.save(notification);
                });
    }

    @Override
    public void readNotification(String notificationId) {
        org.uresti.pozarreal.model.Notification notification = notificationsRepository.findById(notificationId).orElseThrow();

        notification.setSeen(true);

        notificationsRepository.save(notification);
    }

    @Override
    public void removeNotification(String notificationId) {
        notificationsRepository.deleteById(notificationId);
    }
}
