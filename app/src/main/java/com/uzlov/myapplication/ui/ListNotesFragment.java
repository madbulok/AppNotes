package com.uzlov.myapplication.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uzlov.myapplication.Note;
import com.uzlov.myapplication.OnChangeCurrentNote;
import com.uzlov.myapplication.OnDeleteListener;
import com.uzlov.myapplication.OnNoteItemClick;
import com.uzlov.myapplication.OnSaveListener;
import com.uzlov.myapplication.R;
import com.uzlov.myapplication.adapters.NotesAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListNotesFragment extends Fragment implements OnNoteItemClick, OnSaveListener, OnDeleteListener, FragmentResultListener {

    private static OnChangeCurrentNote onChangeCurrentNote;
    private RecyclerView recyclerView;



    private Note currentNote;
    public static final String ARG_INDEX = "index";
    private boolean isLandscape;
    private final List<Note> notes = new ArrayList<>();
    private final NotesAdapter adapter = new NotesAdapter(this);

    public static ListNotesFragment newInstance(OnChangeCurrentNote onChangeNote, Note note) {
        onChangeCurrentNote = onChangeNote;
        ListNotesFragment f = new ListNotesFragment();    // создание
        // Передача параметра
        Bundle args = new Bundle();
        args.putParcelable(ARG_INDEX, note);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

        notes.add(new Note("Заметка 1", "нет ничего", new Date().toString(), "Artem"));
        notes.add(new Note("Заметка 2", "что то", new Date().toString(), "Artem"));
        notes.add(new Note("Заметка 3", "пусто", new Date().toString(), "Artem"));
        notes.add(new Note("Продукты", "молоко11 и хлеб", new Date().toString(), "Vitya"));
        notes.add(new Note("Продукты2", "молоко33 и хлеб55", new Date().toString(), "Kolya"));
        notes.add(new Note("Продукты3", "молоко2 и хлеб3", new Date().toString(), "Pavel"));
        notes.add(new Note("Продукты4", "молоко1 и хлеб4", new Date().toString(), "Django"));
        notes.add(new Note("Заметка 5"));

        getParentFragmentManager().setFragmentResultListener(getString(R.string.key_delete), this, this);
        getParentFragmentManager().setFragmentResultListener(getString(R.string.key_save), this, this);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null && savedInstanceState.getParcelable(ARG_INDEX) != null){
            currentNote = savedInstanceState.getParcelable(ARG_INDEX);
        } else {
            currentNote = new Note("Первая");
        }

        adapter.setNotes(notes);
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.recyclerViewNotes);

        // используем адаптер данных
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_INDEX, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(int position) {
        currentNote = notes.get(position);
        onChangeCurrentNote.changeNote(currentNote);
    }

    @Override
    public void onSave(Note note) {
        notes.add(note);
        adapter.addNote(note);
    }

    @Override
    public void delete(Note note) {
        Log.e(getClass().getSimpleName(), note.getName());

        notes.remove(note);
        adapter.removeNote(note);
    }

    @Override
    public void onDestroy() {
        getParentFragmentManager().clearFragmentResultListener(getString(R.string.key_save));
        getParentFragmentManager().clearFragmentResultListener(getString(R.string.key_delete));
        super.onDestroy();
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        Log.e(getClass().getSimpleName(), requestKey);
        if (requestKey.equals(getString(R.string.key_delete))){
            delete(result.getParcelable(ARG_INDEX));
        } else if (requestKey.equals(getString(R.string.key_save))){
            onSave(result.getParcelable(ARG_INDEX));
        }
    }
}