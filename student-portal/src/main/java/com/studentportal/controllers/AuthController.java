package com.studentportal.controllers;

import com.studentportal.dto.LoginDTO;
import com.studentportal.dto.RegisterDTO;
import com.studentportal.models.Student;
import com.studentportal.services.AuthService;
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
    public String loginPage(HttpSession session) {
        if (session.getAttribute("studentId") != null) return "redirect:/feed";
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO data, HttpSession session, Model model) {
        Student student = authService.login(data);
        if (student == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
        session.setAttribute("studentId", student.getId());
        return "redirect:/feed";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("studentId") != null) return "redirect:/feed";
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDTO data, HttpSession session, Model model) {
        Student student = authService.register(data);
        if (student == null) {
            model.addAttribute("error", "Email already in use");
            model.addAttribute("name", data.getName());
            model.addAttribute("email", data.getEmail());
            model.addAttribute("major", data.getMajor());
            return "register";
        }
        session.setAttribute("studentId", student.getId());
        return "redirect:/feed";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/")
    public String root(HttpSession session) {
        return session.getAttribute("studentId") != null ? "redirect:/feed" : "redirect:/login";
    }
}
