package com.uzlov.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListNotesFragment extends Fragment implements AdapterView.OnItemClickListener , OnChangeCurrentNote{

    private static OnChangeCurrentNote onChangeCurrentNote;
    private ListView lvNotes;

    private Note currentNote;
    public static final String ARG_INDEX = "index";
    private boolean isLandscape;
    private final List<Note> notes = new ArrayList<>();

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
        notes.add(new Note("Заметка 1", "нет ничего", new Date().toString(), "Artem"));
        notes.add(new Note("Заметка 2", "что то", new Date().toString(), "Artem"));
        notes.add(new Note("Заметка 3", "пусто", new Date().toString(), "Artem"));
        notes.add(new Note("Продукты", "молоко и хлеб", new Date().toString(), "Artem"));
        notes.add(new Note("Заметка 5"));
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null && savedInstanceState.getParcelable(ARG_INDEX) != null){
            currentNote = savedInstanceState.getParcelable(ARG_INDEX);
        } else {
            currentNote = new Note("Первая");
        }
    }



    private void initView(View view){
        lvNotes = view.findViewById(R.id.listViewNotes);
        lvNotes.setOnItemClickListener(this);

        final String[] titles = new String[]{"Заметка 1",
                "Заметка 2",
                "Заметка 3",
                "Продукты",
                "Заметка 5"};

        // используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, titles);

        lvNotes.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentNote = notes.get(position);
        onChangeCurrentNote.newCurrentNote(currentNote);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_INDEX, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void newCurrentNote(Note _note) {
        currentNote = _note;
    }
}