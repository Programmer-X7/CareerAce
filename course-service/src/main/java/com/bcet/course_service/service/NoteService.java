package com.bcet.course_service.service;

import java.util.UUID;

import com.bcet.course_service.model.Note;

public interface NoteService {

    Note getNote(String userId, UUID contentId);

    void updateNote(UUID noteId, String noteContent);

    void deleteNote(UUID noteId);

}
