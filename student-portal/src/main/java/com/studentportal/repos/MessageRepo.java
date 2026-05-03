package com.studentportal.repos;

import com.studentportal.models.Message;
import com.studentportal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtDesc(
        Student a, Student b, Student b2, Student a2
    );

    List<Message> findBySender(Student student);

    List<Message> findByReceiver(Student student);
}
