package com.studentportal.services;

import com.studentportal.dto.LoginDTO;
import com.studentportal.dto.RegisterDTO;
import com.studentportal.models.Student;
import com.studentportal.repos.StudentRepo;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final StudentRepo studentRepo;

    AuthService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student register(RegisterDTO data) {
        if (studentRepo.existsByEmail(data.getEmail())) return null;

        Student student = new Student();
        student.loadFromDTO(data);
        student.setEmail(data.getEmail().toLowerCase());
        return studentRepo.save(student);
    }

    public Student login(LoginDTO data) {
        return studentRepo.findByEmail(data.getEmail().toLowerCase())
                .filter(s -> s.getPassword().equals(data.getPassword()))
                .orElse(null);
    }

    public Student getById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
