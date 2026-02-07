package com.cloud.notes.service.cloudnotesapi.notes.dto;

import java.time.Instant;

public class NoteResponse {
    private final String id;
    private final String title;
    private final String content;
    private final Instant createdAt;

    public NoteResponse(String id, String title, String content, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

