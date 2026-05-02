package com.studentportal.controllers;

import com.studentportal.models.Student;
import com.studentportal.repos.NotifRepo;
import com.studentportal.repos.StudentRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final StudentRepo studentRepo;
    private final NotifRepo notifRepo;

    GlobalControllerAdvice(StudentRepo studentRepo, NotifRepo notifRepo) {
        this.studentRepo = studentRepo;
        this.notifRepo = notifRepo;
    }

    @ModelAttribute("currentUser")
    public Student currentUser(HttpSession session) {
        Long id = (Long) session.getAttribute("studentId");
        if (id == null) return null;
        return studentRepo.findById(id).orElse(null);
    }

    @ModelAttribute("unreadNotifCount")
    public long unreadNotifCount(HttpSession session) {
        Long id = (Long) session.getAttribute("studentId");
        if (id == null) return 0L;
        Student student = studentRepo.findById(id).orElse(null);
        if (student == null) return 0L;
        return notifRepo.countByRecipientAndIsReadFalse(student);
    }
}
