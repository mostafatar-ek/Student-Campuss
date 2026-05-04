package com.studentportal.controllers;

import com.studentportal.services.NotifService;
import com.studentportal.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    public String view(HttpServletRequest request, Model model) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        model.addAttribute("notifications", notifService.getForStudent(studentId));
        notifService.markAllAsRead(studentId);
        return "notifications";
    }

    @PostMapping("/notifications/{id}/read")
    public String markOne(@PathVariable Long id, HttpServletRequest request) {
        if (CookieUtil.get(request) == null) return "redirect:/login";
        notifService.markAsRead(id);
        return "redirect:/notifications";
    }
}
