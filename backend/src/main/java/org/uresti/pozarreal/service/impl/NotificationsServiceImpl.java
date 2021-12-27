package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.uresti.pozarreal.dto.Notification;
import org.uresti.pozarreal.repository.NotificationsRepository;
import org.uresti.pozarreal.service.NotificationsService;
import org.uresti.pozarreal.service.mappers.NotificationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationsServiceImpl implements NotificationsService {

    private final NotificationsRepository notificationsRepository;

    public NotificationsServiceImpl(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @Override
    public List<Notification> getNotifications(String userId) {
        return notificationsRepository.findAllByUserId(userId).stream()
                .map(NotificationMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
