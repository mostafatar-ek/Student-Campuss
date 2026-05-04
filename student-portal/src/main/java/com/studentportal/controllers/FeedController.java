package com.studentportal.controllers;

import com.studentportal.dto.AddCommentDTO;
import com.studentportal.dto.CreatePostDTO;
import com.studentportal.services.FeedService;
import com.studentportal.services.PostService;
import com.studentportal.services.StudentService;
import com.studentportal.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FeedController {

    private final FeedService feedService;
    private final PostService postService;
    private final StudentService studentService;

    FeedController(FeedService feedService, PostService postService, StudentService studentService) {
        this.feedService = feedService;
        this.postService = postService;
        this.studentService = studentService;
    }

    @GetMapping("/feed")
    public String feed(@RequestParam(required = false) String tag,
                       HttpServletRequest request, Model model) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        model.addAttribute("currentUser", studentService.getProfile(studentId));
        model.addAttribute("posts", tag != null && !tag.isBlank()
                ? feedService.getFeedByTag(tag, studentId)
                : feedService.getFeed(studentId));
        model.addAttribute("currentTag", tag);
        return "feed";
    }

    @PostMapping("/feed/post")
    public String createPost(@ModelAttribute CreatePostDTO data,
                              @RequestParam(required = false) MultipartFile file,
                              HttpServletRequest request, RedirectAttributes ra) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        try {
            postService.createPost(studentId, data, file);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/feed";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, HttpServletRequest request) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        try { postService.deletePost(id, studentId); } catch (Exception ignored) {}
        return redirectBack(request, "/feed");
    }

    @PostMapping("/posts/{id}/like")
    public String toggleLike(@PathVariable Long id, HttpServletRequest request) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        try { postService.toggleLike(id, studentId); } catch (Exception ignored) {}
        return redirectBack(request, "/feed");
    }

    @PostMapping("/posts/{id}/comment")
    public String addComment(@PathVariable Long id,
                              @ModelAttribute AddCommentDTO data,
                              HttpServletRequest request) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        try { postService.addComment(id, studentId, data); } catch (Exception ignored) {}
        return redirectBack(request, "/feed");
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                 HttpServletRequest request) {
        Long studentId = CookieUtil.get(request);
        if (studentId == null) return "redirect:/login";
        try { postService.deleteComment(commentId, studentId); } catch (Exception ignored) {}
        return redirectBack(request, "/feed");
    }

    private String redirectBack(HttpServletRequest request, String fallback) {
        String ref = request.getHeader("Referer");
        return "redirect:" + (ref != null ? ref : fallback);
    }
}
