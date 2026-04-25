package com.kalasetu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String subject;

    @Column(length = 2000)
    private String message;

    private LocalDateTime createdAt;

    private String status; // PENDING, RESOLVED, DISMISSED

    public Complaint() {}

    public Complaint(User user, String subject, String message) {
        this.user = user;
        this.subject = subject;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
