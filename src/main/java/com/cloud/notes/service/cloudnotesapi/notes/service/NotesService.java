package com.cloud.notes.service.cloudnotesapi.notes.service;

import com.cloud.notes.service.cloudnotesapi.notes.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotesService {

    private static final Logger log = LoggerFactory.getLogger(NotesService.class);

    private final ConcurrentHashMap<String, Note> store = new ConcurrentHashMap<>();

    public Note create(String title, String content) {
        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();
        Note note = new Note(id, title, content, createdAt);
        store.put(id, note);

        log.info("Note created id={} title={}", id, title);
        return note;
    }

    public List<Note> list() {
        List<Note> notes = new ArrayList<>(store.values());
        notes.sort(Comparator.comparing(Note::getCreatedAt).reversed());
        return notes;
    }

    public Optional<Note> get(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public boolean delete(String id) {
        Note removed = store.remove(id);
        if (removed != null) {
            log.info("Note deleted id={}", id);
            return true;
        }
        return false;
    }
}

