package com.student_portal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String profilePic;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}