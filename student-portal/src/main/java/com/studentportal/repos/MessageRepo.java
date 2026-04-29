package com.studentportal.repos;

import com.studentportal.models.Message;
import com.studentportal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender = :a AND m.receiver = :b) OR (m.sender = :b AND m.receiver = :a) " +
           "ORDER BY m.sentAt DESC")
    List<Message> findConversation(@Param("a") Student a, @Param("b") Student b);

    @Query("SELECT DISTINCT m.receiver FROM Message m WHERE m.sender = :student")
    List<Student> findReceivers(@Param("student") Student student);

    @Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver = :student")
    List<Student> findSenders(@Param("student") Student student);
}
