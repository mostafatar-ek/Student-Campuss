package com.studentportal.controllers;

import com.studentportal.services.FeedService;
import com.studentportal.services.StudentService;
import com.studentportal.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentMvcController {

    private final StudentService studentService;
    private final FeedService feedService;

    StudentMvcController(StudentService studentService, FeedService feedService) {
        this.studentService = studentService;
        this.feedService = feedService;
    }

    @GetMapping("/students")
    public String search(@RequestParam(required = false) String q,
                         HttpServletRequest request, Model model) {
        if (CookieUtil.get(request) == null) return "redirect:/login";
        model.addAttribute("students", studentService.searchStudents(q));
        model.addAttribute("query", q);
        return "search";
    }

    @GetMapping("/students/{id}")
    public String studentProfile(@PathVariable Long id, HttpServletRequest request, Model model) {
        Long myId = CookieUtil.get(request);
        if (myId == null) return "redirect:/login";
        model.addAttribute("student", studentService.getProfile(id));
        model.addAttribute("posts", feedService.getFeed(myId)
                .stream().filter(p -> p.getAuthor().getId().equals(id)).toList());
        return "student-profile";
    }
}
