package com.uzlov.myapplication.ui;

import com.uzlov.myapplication.model.Note;

@FunctionalInterface
public interface OnSaveListener {
    void onSave(Note note);
}
