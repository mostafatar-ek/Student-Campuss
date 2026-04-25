package com.student_portal.repositories;

import com.student_portal.models.Comment;
import com.student_portal.models.Post;
import com.student_portal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
    List<Comment> findByStudent(Student student);
}