package com.studentportal.controllers;

import com.studentportal.services.NotifService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NotifMvcController {

    private final NotifService notifService;

    NotifMvcController(NotifService notifService) {
        this.notifService = notifService;
    }

    @GetMapping("/notifications")
    public String view(HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) return "redirect:/login";
        model.addAttribute("notifications", notifService.getForStudent(studentId));
        notifService.markAllAsRead(studentId);
        return "notifications";
    }

    @PostMapping("/notifications/{id}/read")
    public String markOne(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("studentId") == null) return "redirect:/login";
        notifService.markAsRead(id);
        return "redirect:/notifications";
    }
}
