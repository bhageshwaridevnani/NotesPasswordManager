package com.example.demo.Scheduler;

import com.example.demo.Model.EntityUser;
import com.example.demo.Repositroy.UserRepository;
import com.example.demo.Socket.NotificationEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationEventHandler notificationEventHandler;

    @Scheduled(cron = "0 46 12 * * *") // Run every day at midnight
    public void sendNotifications() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<EntityUser> users = userRepository.findByCreatedAtBefore(thirtyDaysAgo);
        for (EntityUser user : users) {
            notificationEventHandler.sendNotificationToUser(user);
        }
    }
}