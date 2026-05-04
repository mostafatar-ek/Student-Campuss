package com.studentportal.controllers;

import com.studentportal.dto.SendMessageDTO;
import com.studentportal.models.Student;
import com.studentportal.repos.StudentRepo;
import com.studentportal.services.ChatService;
import com.studentportal.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final StudentRepo studentRepo;

    ChatController(ChatService chatService, StudentRepo studentRepo) {
        this.chatService = chatService;
        this.studentRepo = studentRepo;
    }

    @GetMapping("/chat")
    public String conversations(HttpServletRequest request, Model model) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        model.addAttribute("conversations", chatService.getConversationPartners(studentId));
        return "chat";
    }

    @GetMapping("/chat/{partnerId}")
    public String conversation(@PathVariable Long partnerId, HttpServletRequest request, Model model) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        Student partner = studentRepo.findById(partnerId).orElse(null);
        if (partner == null) return "redirect:/chat";
        model.addAttribute("conversations", chatService.getConversationPartners(studentId));
        model.addAttribute("partner", partner);
        model.addAttribute("messages", chatService.getConversation(studentId, partnerId));
        return "chat";
    }

    @PostMapping("/chat/{partnerId}/send")
    public String sendMessage(@PathVariable Long partnerId,
                               @ModelAttribute SendMessageDTO data,
                               @RequestParam(required = false) MultipartFile file,
                               HttpServletRequest request) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        try { chatService.sendMessage(studentId, partnerId, data, file); } catch (Exception ignored) {}
        return "redirect:/chat/" + partnerId;
    }
}
