package com.peakform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProgressNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private String author;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ProgressNote() {}

    public ProgressNote(Long memberId, String author, String content) {
        this.memberId = memberId;
        this.author = author;
        this.content = content;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
