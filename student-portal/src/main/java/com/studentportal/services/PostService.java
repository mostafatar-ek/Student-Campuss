package com.studentportal.services;

import com.studentportal.dto.AddCommentDTO;
import com.studentportal.dto.CreatePostDTO;
import com.studentportal.models.*;
import com.studentportal.repos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final LikeRepo likeRepo;
    private final StudentRepo studentRepo;
    private final NotifService notifService;
    private final StudentService studentService;

    PostService(PostRepo postRepo, CommentRepo commentRepo, LikeRepo likeRepo,
                StudentRepo studentRepo, NotifService notifService, StudentService studentService) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.likeRepo = likeRepo;
        this.studentRepo = studentRepo;
        this.notifService = notifService;
        this.studentService = studentService;
    }

    public Post createPost(Long authorId, CreatePostDTO data, MultipartFile file) throws IOException {
        Student author = studentRepo.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        String attachment = (file != null && !file.isEmpty()) ? studentService.saveFile(file) : null;
        Post post = new Post(author, data.getContent(), attachment, data.getSubjectTag());
        return postRepo.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Long requesterId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getAuthor().getId().equals(requesterId))
            throw new RuntimeException("Not authorized to delete this post");
        likeRepo.deleteByPostId(postId);
        commentRepo.deleteByPostId(postId);
        postRepo.deleteById(postId);
    }

    public Comment addComment(Long postId, Long authorId, AddCommentDTO data) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Student author = studentRepo.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Comment comment = commentRepo.save(new Comment(post, author, data.getContent()));

        if (!post.getAuthor().getId().equals(authorId)) {
            notifService.create(post.getAuthor().getId(), "COMMENT",
                    author.getName() + " commented on your post", post.getId());
        }
        return comment;
    }

    public void deleteComment(Long commentId, Long requesterId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getAuthor().getId().equals(requesterId))
            throw new RuntimeException("Not authorized to delete this comment");
        commentRepo.deleteById(commentId);
    }

    public List<Comment> getComments(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return commentRepo.findByPostOrderByCreatedAtAsc(post);
    }

    public boolean toggleLike(Long postId, Long studentId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Optional<Like> existing = likeRepo.findByStudentAndPost(student, post);
        if (existing.isPresent()) {
            likeRepo.delete(existing.get());
            return false;
        }
        likeRepo.save(new Like(student, post));
        if (!post.getAuthor().getId().equals(studentId)) {
            notifService.create(post.getAuthor().getId(), "LIKE",
                    student.getName() + " liked your post", post.getId());
        }
        return true;
    }

    public long getLikeCount(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return likeRepo.countByPost(post);
    }
}
