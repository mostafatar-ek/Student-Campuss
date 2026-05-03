package com.studentportal.services;

import com.studentportal.models.Post;
import com.studentportal.repos.CommentRepo;
import com.studentportal.repos.LikeRepo;
import com.studentportal.repos.PostRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedService {

    private final PostRepo postRepo;
    private final LikeRepo likeRepo;
    private final CommentRepo commentRepo;

    FeedService(PostRepo postRepo, LikeRepo likeRepo, CommentRepo commentRepo) {
        this.postRepo = postRepo;
        this.likeRepo = likeRepo;
        this.commentRepo = commentRepo;
    }

    public List<Post> getFeed(Long currentStudentId) {
        return enrichPosts(postRepo.findAllByOrderByCreatedAtDesc(), currentStudentId);
    }

    public List<Post> getFeedByTag(String tag, Long currentStudentId) {
        return enrichPosts(postRepo.findBySubjectTagIgnoreCaseOrderByCreatedAtDesc(tag), currentStudentId);
    }

    private List<Post> enrichPosts(List<Post> posts, Long currentStudentId) {
        posts.forEach(post -> {
            post.setLikeCount((int) likeRepo.countByPost(post));
            post.setCommentCount((int) commentRepo.countByPost(post));
            if (currentStudentId != null)
                post.setLikedByCurrentUser(likeRepo.existsByStudentIdAndPostId(currentStudentId, post.getId()));
        });
        return posts;
    }
}
