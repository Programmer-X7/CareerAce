package com.bcet.course_service.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcet.course_service.dto.APIResponse;
import com.bcet.course_service.service.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<?> getNote(@RequestParam(name = "uid") String userId,
            @RequestParam(name = "cid") UUID contentId) {
        try {
            return ResponseEntity.ok(noteService.getNote(userId, contentId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error fetching note"));
        }
    }

    @PutMapping("/update/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable UUID noteId, @RequestBody String noteContent) {
        try {
            noteService.updateNote(noteId, noteContent);
            return ResponseEntity.ok(new APIResponse("Note updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error updating note"));
        }
    }

    @DeleteMapping("/delete/{noteId}")
    public ResponseEntity<APIResponse> deleteNote(@PathVariable UUID noteId) {
        try {
            noteService.deleteNote(noteId);
            return new ResponseEntity<>(new APIResponse("Note deleted successfully"), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse("Error deleting note"));
        }
    }
}
