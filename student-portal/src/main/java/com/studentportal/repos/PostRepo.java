package com.studentportal.repos;

import com.studentportal.models.Post;
import com.studentportal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findBySubjectTagIgnoreCaseOrderByCreatedAtDesc(String tag);
    List<Post> findByAuthorOrderByCreatedAtDesc(Student author);
}
