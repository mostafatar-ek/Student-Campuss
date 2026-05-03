package com.studentportal.repos;

import com.studentportal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByMajorContainingIgnoreCase(String major);
}
