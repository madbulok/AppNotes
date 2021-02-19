package com.uzlov.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;


public class NoteFragment extends Fragment{

    private TextView tvDate;
    private TextView tvName;
    private TextView tvAuthor;
    private EditText etDescription;
    private FloatingActionButton fabShareNote;
    private AppCompatImageView imageNote;
    private Toolbar toolbar;

    private static final String ARG_INDEX = "index";
    private Note note;

    public static NoteFragment newInstance(Note note) {
        NoteFragment f = new NoteFragment();
        // Передача параметра
        Bundle args = new Bundle();
        args.putParcelable(ARG_INDEX, note);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelable(ARG_INDEX) != null) {
            note = getArguments().getParcelable(ARG_INDEX);
        } else {
            note = new Note("Первая");
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
        initListeners();
        fillView();
    }

    private void initView(View view){
        tvDate = view.findViewById(R.id.textview_date);
        tvName = view.findViewById(R.id.textViewName);
        tvAuthor = view.findViewById(R.id.textView_author);
        etDescription = view.findViewById(R.id.etDescription);
        imageNote = view.findViewById(R.id.iv_image_note);
        fabShareNote = view.findViewById(R.id.fab_share_note);
        toolbar = view.findViewById(R.id.toolbar);
    }

    private void initListeners() {
        tvDate.setOnClickListener(v -> openTimePicker());

        fabShareNote.setOnClickListener(v -> startShareIntent());
    }

    private void startShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, etDescription.getText().toString());

        Intent chooser = Intent.createChooser(shareIntent, "Выбор приложения");
        startActivity(chooser);
    }

    private void openTimePicker() {
        MaterialTimePicker picker =
                new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText(R.string.select_new_string)
                        .build();

        if (getActivity() != null){
            picker.show(getActivity().getSupportFragmentManager(), "datePicker");
        }

        picker.addOnPositiveButtonClickListener(v1 -> {
            tvDate.setText(picker.getHour()+":" + picker.getMinute());
        });
    }

    private void fillView() {
        tvAuthor.setText(note.getAuthor());
        tvDate.setText(note.getDateCreate());
        tvName.setText(note.getName());
        etDescription.setText(note.getDescription());
        toolbar.setTitle(note.getName());
    }
}