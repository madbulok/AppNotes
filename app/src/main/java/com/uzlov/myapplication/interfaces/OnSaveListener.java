package com.uzlov.myapplication.interfaces;

import com.uzlov.myapplication.Note;

@FunctionalInterface
public interface OnSaveListener {
    void onSave(Note note);
}
