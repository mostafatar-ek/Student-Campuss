package com.studentportal.controllers;

import com.studentportal.dto.LoginDTO;
import com.studentportal.dto.RegisterDTO;
import com.studentportal.models.Student;
import com.studentportal.services.AuthService;
import com.studentportal.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        if (CookieUtil.get(request) != null) return "redirect:/feed";
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO data,
                        HttpServletResponse response, Model model) {
        Student student = authService.login(data);
        if (student == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
        if (!student.isVerified()) {
            model.addAttribute("error", "Please verify your email before logging in");
            return "login";
        }
        CookieUtil.set(response, student.getId());
        return "redirect:/feed";
    }

    @GetMapping("/register")
    public String registerPage(HttpServletRequest request) {
        if (CookieUtil.get(request) != null) return "redirect:/feed";
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDTO data,
                           HttpServletRequest request, HttpSession session, Model model) {
        if (CookieUtil.get(request) != null) return "redirect:/feed";
        Student student = authService.register(data);
        if (student == null) {
            model.addAttribute("error", "Email already in use");
            model.addAttribute("name", data.getName());
            model.addAttribute("email", data.getEmail());
            model.addAttribute("major", data.getMajor());
            return "register";
        }
        session.setAttribute("pendingStudentId", student.getId());
        return "redirect:/verify";
    }

    @GetMapping("/verify")
    public String verifyPage(HttpSession session, HttpServletRequest request, Model model) {
        if (CookieUtil.get(request) != null) return "redirect:/feed";
        Long pendingId = (Long) session.getAttribute("pendingStudentId");
        if (pendingId == null) return "redirect:/register";
        model.addAttribute("email", authService.getById(pendingId).getEmail());
        return "verify";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String code,
                         HttpSession session, HttpServletResponse response, Model model) {
        Long pendingId = (Long) session.getAttribute("pendingStudentId");
        if (pendingId == null) return "redirect:/register";
        if (!authService.verify(pendingId, code)) {
            model.addAttribute("error", "Invalid code. Please try again.");
            model.addAttribute("email", authService.getById(pendingId).getEmail());
            return "verify";
        }
        session.removeAttribute("pendingStudentId");
        CookieUtil.set(response, pendingId);
        return "redirect:/feed";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        CookieUtil.clear(response);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String root(HttpServletRequest request) {
        return CookieUtil.get(request) != null ? "redirect:/feed" : "redirect:/login";
    }
}
