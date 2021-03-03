package com.uzlov.myapplication.repositories.notes;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uzlov.myapplication.model.Note;
import com.uzlov.myapplication.repositories.Constants;

import java.util.List;

public class NotesRepositoryImpl implements NotesRepository {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final NotesFirestoreRepository callbacks;

    public NotesRepositoryImpl(NotesFirestoreRepository callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void loadNotes() {
        firebaseFirestore
                .collection(Constants.TABLE_NAME_NOTES)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        List<Note> items = task.getResult().toObjects(Note.class);
                        callbacks.loadSuccess(items);
                    }
                })
                .addOnFailureListener(e -> callbacks.loadFailed(e.getMessage()));
    }

    @Override
    public void deleteNote(@NonNull String id) {
        firebaseFirestore
                .collection(Constants.TABLE_NAME_NOTES)
                .document(id)
                .delete()
                .addOnSuccessListener(aVoid -> loadNotes());
    }
}
