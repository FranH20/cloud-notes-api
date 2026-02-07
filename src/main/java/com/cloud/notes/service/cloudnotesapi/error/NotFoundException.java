package com.cloud.notes.service.cloudnotesapi.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

