package com.studentportal.repos;

import com.studentportal.models.Like;
import com.studentportal.models.Post;
import com.studentportal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long> {
    Optional<Like> findByStudentAndPost(Student student, Post post);
    long countByPost(Post post);
    boolean existsByStudentAndPost(Student student, Post post);
    boolean existsByStudentIdAndPostId(Long studentId, Long postId);
    void deleteByPostId(Long postId);
}
