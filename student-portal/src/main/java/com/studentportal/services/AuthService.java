package com.studentportal.services;

import com.studentportal.dto.LoginDTO;
import com.studentportal.dto.RegisterDTO;
import com.studentportal.models.Student;
import com.studentportal.repos.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    private final StudentRepo studentRepo;
    private final EmailService emailService;

    AuthService(StudentRepo studentRepo, EmailService emailService) {
        this.studentRepo = studentRepo;
        this.emailService = emailService;
    }

    public Student register(RegisterDTO data) {
        if (studentRepo.existsByEmail(data.getEmail().toLowerCase())) return null;

        Student student = new Student();
        student.loadFromDTO(data);
        student.setEmail(data.getEmail().toLowerCase());
        student.setVerified(false);
        student.setVerificationCode(String.format("%06d", new Random().nextInt(1_000_000)));

        Student saved = studentRepo.save(student);
        emailService.sendVerificationCode(saved.getEmail(), saved.getVerificationCode());
        return saved;
    }

    public Student login(LoginDTO data) {
        return studentRepo.findByEmail(data.getEmail().toLowerCase())
                .filter(s -> s.getPassword().equals(data.getPassword()))
                .orElse(null);
    }

    public boolean verify(Long studentId, String code) {
        return studentRepo.findById(studentId).map(student -> {
            if (code != null && code.equals(student.getVerificationCode())) {
                student.setVerified(true);
                student.setVerificationCode(null);
                studentRepo.save(student);
                return true;
            }
            return false;
        }).orElse(false);
    }

    public Student getById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
