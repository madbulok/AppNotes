package com.uzlov.myapplication.repositories.notes;

import com.uzlov.myapplication.model.Note;

import java.util.List;

public interface NotesFirestoreRepository {

    void loadSuccess(List<Note> result);
    void loadFailed(String message);
}
