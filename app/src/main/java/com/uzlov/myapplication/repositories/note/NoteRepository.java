package com.uzlov.myapplication.repositories.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface NoteRepository {

    void setNote(
        @NonNull String id,
        @NonNull String title,
        @NonNull String description,
        @Nullable String autor,
        @Nullable String datetime
    );

    void onDeleteClicked(@NonNull String id);
}
