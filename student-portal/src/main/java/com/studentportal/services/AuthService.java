package com.studentportal.services;

import com.studentportal.models.Student;
import com.studentportal.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private StudentRepo studentRepo;

    public Student register(String name, String email, String password, String major) {
        if (studentRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        if (name == null || name.isBlank()) throw new RuntimeException("Name is required");
        if (email == null || email.isBlank()) throw new RuntimeException("Email is required");
        if (password == null || password.length() < 6) throw new RuntimeException("Password must be at least 6 characters");

        Student student = new Student(name.trim(), email.trim().toLowerCase(), password, major);
        return studentRepo.save(student);
    }

    public Student login(String email, String password) {
        Student student = studentRepo.findByEmail(email.trim().toLowerCase())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!student.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }
        return student;
    }

    public Student getById(Long id) {
        return studentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}