package com.uzlov.myapplication.repositories.notes;

import androidx.annotation.NonNull;

public interface NotesRepository {
    void loadNotes();
    void deleteNote(@NonNull String id);
}
