package com.cloud.notes.service.cloudnotesapi.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateNoteRequest {

    @NotBlank(message = "title is required")
    @Size(max = 100, message = "title must be at most 100 characters")
    private String title;

    private String content;

    public CreateNoteRequest() {
    }

    public CreateNoteRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

