package com.student_portal.repositories;

import com.student_portal.models.Post;
import com.student_portal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByStudent(Student student);
    List<Post> findBySubject(String subject);
    List<Post> findAllByOrderByCreatedAtDesc();
}