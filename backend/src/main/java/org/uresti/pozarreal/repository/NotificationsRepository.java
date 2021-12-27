package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.Notification;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, String> {
    List<Notification> findAllByUserId(String userId);
}
