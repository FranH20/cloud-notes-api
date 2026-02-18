package com.cloud.notes.service.cloudnotesapi.notes.controller;

import com.cloud.notes.service.cloudnotesapi.error.NotFoundException;
import com.cloud.notes.service.cloudnotesapi.notes.dto.CreateNoteRequest;
import com.cloud.notes.service.cloudnotesapi.notes.dto.NoteResponse;
import com.cloud.notes.service.cloudnotesapi.notes.model.Note;
import com.cloud.notes.service.cloudnotesapi.notes.service.NotesService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/notes", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotesController {

    private static final Logger log = LoggerFactory.getLogger(NotesController.class);

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@Valid @RequestBody CreateNoteRequest request) {
        int contentLen = request.getContent() == null ? 0 : request.getContent().length();
        log.info("notes.create title=\"{}\" contentLength={}", request.getTitle(), contentLen);

        Note note = notesService.create(request.getTitle(), request.getContent());
        return toResponse(note);
    }

    @GetMapping
    public List<NoteResponse> list() {
        List<NoteResponse> result = notesService.list().stream().map(this::toResponse).toList();
        log.debug("notes.list count={}", result.size());
        return result;
    }

    @GetMapping("/{id}")
    public NoteResponse get(@PathVariable String id) {
        log.debug("notes.get id={}", id);
        Note note = notesService.get(id).orElseThrow(() -> new NotFoundException("Note not found: " + id));
        return toResponse(note);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        log.info("notes.delete id={}", id);
        boolean deleted = notesService.delete(id);
        if (!deleted) {
            throw new NotFoundException("Note not found: " + id);
        }
    }

    private NoteResponse toResponse(Note note) {
        return new NoteResponse(note.getId(), note.getTitle(), note.getContent(), note.getCreatedAt());
    }
}
