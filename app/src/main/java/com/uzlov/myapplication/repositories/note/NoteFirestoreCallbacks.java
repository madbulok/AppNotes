package com.uzlov.myapplication.repositories.note;

import androidx.annotation.Nullable;

public interface NoteFirestoreCallbacks {

    void onSuccess(@Nullable String message);
    void onError(@Nullable String message);
}
