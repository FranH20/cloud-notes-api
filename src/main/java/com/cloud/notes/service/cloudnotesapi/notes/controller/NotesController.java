package com.cloud.notes.service.cloudnotesapi.notes.controller;

import com.cloud.notes.service.cloudnotesapi.error.NotFoundException;
import com.cloud.notes.service.cloudnotesapi.notes.dto.CreateNoteRequest;
import com.cloud.notes.service.cloudnotesapi.notes.dto.NoteResponse;
import com.cloud.notes.service.cloudnotesapi.notes.model.Note;
import com.cloud.notes.service.cloudnotesapi.notes.service.NotesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/notes", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@Valid @RequestBody CreateNoteRequest request) {
        Note note = notesService.create(request.getTitle(), request.getContent());
        return toResponse(note);
    }

    @GetMapping
    public List<NoteResponse> list() {
        return notesService.list().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public NoteResponse get(@PathVariable String id) {
        Note note = notesService.get(id).orElseThrow(() -> new NotFoundException("Note not found: " + id));
        return toResponse(note);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        boolean deleted = notesService.delete(id);
        if (!deleted) {
            throw new NotFoundException("Note not found: " + id);
        }
    }

    private NoteResponse toResponse(Note note) {
        return new NoteResponse(note.getId(), note.getTitle(), note.getContent(), note.getCreatedAt());
    }
}

