package com.studentportal.repos;

import com.studentportal.models.Notification;
import com.studentportal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotifRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(Student recipient);
    long countByRecipientAndIsReadFalse(Student recipient);
}
