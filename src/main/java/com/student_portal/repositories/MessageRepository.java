package com.student_portal.repositories;

import com.student_portal.models.Message;
import com.student_portal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiver(Student sender, Student receiver);
    List<Message> findByReceiverAndIsReadFalse(Student receiver);
    int countByReceiverAndIsReadFalse(Student receiver);
}