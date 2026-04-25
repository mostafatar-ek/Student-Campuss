package com.student_portal.repositories;

import com.student_portal.models.Like;
import com.student_portal.models.Post;
import com.student_portal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByStudentAndPost(Student student, Post post);
    int countByPost(Post post);
    boolean existsByStudentAndPost(Student student, Post post);
    void deleteByStudentAndPost(Student student, Post post);
}