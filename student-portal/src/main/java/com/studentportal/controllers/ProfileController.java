package com.studentportal.controllers;

import com.studentportal.dto.EditProfileDTO;
import com.studentportal.services.FeedService;
import com.studentportal.services.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final StudentService studentService;
    private final FeedService feedService;

    ProfileController(StudentService studentService, FeedService feedService) {
        this.studentService = studentService;
        this.feedService = feedService;
    }

    @GetMapping
    public String view(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) return "redirect:/login";
        model.addAttribute("student", studentService.getProfile(studentId));
        model.addAttribute("posts", feedService.getFeed(studentId)
                .stream().filter(p -> p.getAuthor().getId().equals(studentId)).toList());
        return "profile";
    }

    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) return "redirect:/login";
        model.addAttribute("student", studentService.getProfile(studentId));
        return "profile-edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute EditProfileDTO data, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) return "redirect:/login";
        studentService.updateProfile(studentId, data);
        return "redirect:/profile";
    }

    @PostMapping("/avatar")
    public String uploadAvatar(@RequestParam MultipartFile file, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) return "redirect:/login";
        try { studentService.uploadAvatar(studentId, file); } catch (Exception ignored) {}
        return "redirect:/profile";
    }
}
