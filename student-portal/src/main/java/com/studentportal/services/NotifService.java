package com.studentportal.services;

import com.studentportal.models.Notification;
import com.studentportal.models.Student;
import com.studentportal.repos.NotifRepo;
import com.studentportal.repos.StudentRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotifService {

    private final NotifRepo notifRepo;
    private final StudentRepo studentRepo;

    NotifService(NotifRepo notifRepo, StudentRepo studentRepo) {
        this.notifRepo = notifRepo;
        this.studentRepo = studentRepo;
    }

    public void create(Long recipientId, String type, String message, Long relatedId) {
        Student recipient = studentRepo.findById(recipientId).orElse(null);
        if (recipient == null) return;
        notifRepo.save(new Notification(recipient, type, message, relatedId));
    }

    public List<Notification> getForStudent(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return notifRepo.findByRecipientOrderByCreatedAtDesc(student);
    }

    public long countUnread(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return notifRepo.countByRecipientAndIsReadFalse(student);
    }

    public void markAsRead(Long notifId) {
        Notification notif = notifRepo.findById(notifId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notif.setRead(true);
        notifRepo.save(notif);
    }

    public void markAllAsRead(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Notification> notifs = notifRepo.findByRecipientOrderByCreatedAtDesc(student);
        notifs.forEach(n -> n.setRead(true));
        notifRepo.saveAll(notifs);
    }
}
