package com.bcet.course_service.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bcet.course_service.model.Note;
import com.bcet.course_service.repository.NoteRepository;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note getNote(String userId, UUID contentId) {
        try {
            if (noteRepository.existsByUserIdAndContentId(userId, contentId)) {
                return noteRepository.findByUserIdAndContentId(userId, contentId).get();
            } else {
                // if not didn't exist then create one
                Note note = new Note();
                note.setUserId(userId);
                note.setContentId(contentId);
                note.setNoteText("");
                noteRepository.save(note);
                return note;
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateNote(UUID noteId, String noteContent) {
        try {
            Optional<Note> optionalNote = noteRepository.findById(noteId);

            if (!optionalNote.isPresent()) {
                logger.error("No note found with ID: " + noteId);
                throw new RuntimeException("No note found with ID: " + noteId);
            }

            Note note = optionalNote.get();
            note.setNoteText(noteContent);
            noteRepository.save(note);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteNote(UUID noteId) {
        try {
            Optional<Note> optionalNote = noteRepository.findById(noteId);
            if (optionalNote.isPresent()) {
                noteRepository.deleteById(optionalNote.get().getNoteId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
