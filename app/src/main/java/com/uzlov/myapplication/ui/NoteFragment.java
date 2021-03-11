package com.uzlov.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.uzlov.myapplication.model.Note;
import com.uzlov.myapplication.R;
import com.uzlov.myapplication.repositories.note.NoteFirestoreCallbacks;
import com.uzlov.myapplication.repositories.note.NoteRepository;
import com.uzlov.myapplication.repositories.note.NoteRepositoryImpl;

import java.util.UUID;


public class NoteFragment extends Fragment implements NoteFirestoreCallbacks {

    private TextView tvDate;
    private TextView tvName;
    private TextView tvAuthor;
    private EditText etDescription;
    private MaterialButton btnShareNote;
    private MaterialButton btnSaveNote;
    private MaterialButton btnDeleteNote;
    private AppCompatImageView imageNote;

    private static final String ARG_INDEX = "index";
    private Note note;
    private final NoteRepository repository = new NoteRepositoryImpl(this);

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
            note = new Note("Первая", UUID.randomUUID().toString());
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
        btnShareNote = view.findViewById(R.id.fab_share_note);
        btnDeleteNote = view.findViewById(R.id.btnDeleteNote);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);
    }

    private void initListeners() {
        tvDate.setOnClickListener(v -> openTimePicker());
        btnShareNote.setOnClickListener(v -> startShareIntent());

        btnSaveNote.setOnClickListener(v -> {
            Note note = new Note(UUID.randomUUID().toString(),
                    tvName.getText().toString(),
                    etDescription.getText().toString(),
                    tvDate.getText().toString(),
                    tvAuthor.getText().toString());

            Bundle result = new Bundle();
            result.putParcelable(ARG_INDEX, note);

            final String name = note.getName();
            final String description = note.getDescription();
            final String author = note.getAuthor();
            final String datetime = note.getDateCreate();
            update(name, description, author, datetime);
        });

        btnDeleteNote.setOnClickListener(v -> {
            repository.onDeleteClicked(note.getId());
            requireActivity().onBackPressed();
        });
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

        picker.addOnPositiveButtonClickListener(v1 ->
                tvDate.setText(picker.getHour() + ":" + picker.getMinute()));
    }

    private void fillView() {
        tvAuthor.setText(note.getAuthor());
        tvDate.setText(note.getDateCreate());
        tvName.setText(note.getName());
        etDescription.setText(note.getDescription());
    }

    private void update(@NonNull String title,
                        @NonNull String description,
                        @NonNull String author,
                        @NonNull String datetime) {
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
            if (getArguments() != null) {
                Note note = (Note) getArguments().getParcelable(ARG_INDEX);
                if (note != null) {
                    repository.setNote(note.getId(), title, description, author, datetime);
                } else {
                    String id = UUID.randomUUID().toString();
                    repository.setNote(id, title, description, author, datetime);
                }
            }
        } else {
            showToastMessage(getString(R.string.fields_do_not_empty));
        }
    }

    private void showToastMessage(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@Nullable String message) {
        showToastMessage(message);
    }

    @Override
    public void onError(@Nullable String message) {
        showToastMessage(message);
    }
}