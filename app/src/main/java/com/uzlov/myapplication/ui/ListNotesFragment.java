package com.uzlov.myapplication.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uzlov.myapplication.OnChangeCurrentNote;
import com.uzlov.myapplication.R;
import com.uzlov.myapplication.adapters.NotesAdapter;
import com.uzlov.myapplication.interfaces.OnDeleteListener;
import com.uzlov.myapplication.interfaces.OnNoteItemClick;
import com.uzlov.myapplication.model.Note;
import com.uzlov.myapplication.repositories.notes.NotesFirestoreRepository;
import com.uzlov.myapplication.repositories.notes.NotesRepository;
import com.uzlov.myapplication.repositories.notes.NotesRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListNotesFragment extends Fragment implements OnNoteItemClick, OnSaveListener, OnDeleteListener, FragmentResultListener, NotesFirestoreRepository {

    private static OnChangeCurrentNote onChangeCurrentNote;

    private final NotesRepository repository = new NotesRepositoryImpl(this);

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
        getParentFragmentManager().setFragmentResultListener(getString(R.string.key_delete), this, this);
        getParentFragmentManager().setFragmentResultListener(getString(R.string.key_save), this, this);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null && savedInstanceState.getParcelable(ARG_INDEX) != null){
            currentNote = savedInstanceState.getParcelable(ARG_INDEX);
        } else {
            currentNote = new Note(UUID.randomUUID().toString(), "Первая");
        }
        repository.loadNotes();
    }

    private void initView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);

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


    @Override
    public void loadSuccess(List<Note> result) {
        notes.clear();
        adapter.setNotes(result);
        notes.addAll(result);
    }

    @Override
    public void loadFailed(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}