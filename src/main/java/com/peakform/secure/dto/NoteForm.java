package com.peakform.secure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class NoteForm {

    @NotBlank(message = "Author name is required")
    @Size(max = 100, message = "Author name must be under 100 characters")
    @Pattern(regexp = "^[\\p{L}0-9 .'-]+$", message = "Author name contains invalid characters")
    private String author;

    @NotNull(message = "A member must be selected")
    private Long memberId;

    @NotBlank(message = "Note content is required")
    @Size(max = 2000, message = "Note content must be under 2000 characters")
    @Pattern(regexp = "^[^<>]*$", message = "Note content cannot contain '<' or '>' characters")
    private String content;

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
