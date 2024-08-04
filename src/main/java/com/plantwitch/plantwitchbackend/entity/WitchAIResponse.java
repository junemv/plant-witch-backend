package com.plantwitch.plantwitchbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "witch")
public class WitchAIResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(columnDefinition="TEXT", name = "prompt", nullable = false)
    private String prompt;

    @Column(columnDefinition="TEXT", name = "response", nullable = false)
    private String response;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public WitchAIResponse() {

    }

    public WitchAIResponse(Long id, User user, String promt, String response, LocalDateTime timestamp) {
        this.id = id;
        this.user = user;
        this.prompt = prompt;
        this.response = response;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
