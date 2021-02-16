package com.uzlov.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class NoteFragment extends Fragment {

    private TextView tvDate;
    private TextView tvName;
    private TextView tvAuthor;
    private EditText etDescription;

    private static final String ARG_INDEX = "index";
    private Note index;

    public static NoteFragment newInstance(Note note) {
        NoteFragment f = new NoteFragment();    // создание

        // Передача параметра
        Bundle args = new Bundle();
        args.putParcelable(ARG_INDEX, note);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getParcelable(ARG_INDEX);
        } else {
            index = new Note("Первая");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fillView();
    }

    private void initView(View view){
        tvDate = view.findViewById(R.id.textview_date);
        tvName = view.findViewById(R.id.textViewName);
        tvAuthor = view.findViewById(R.id.textView_author);
        etDescription = view.findViewById(R.id.etDescription);
    }

    private void fillView() {
        etDescription.setText(index.getDescription());
        tvAuthor.setText(index.getAuthor());
        tvDate.setText(index.getDateCreate());
        tvName.setText(index.getName());
    }
}