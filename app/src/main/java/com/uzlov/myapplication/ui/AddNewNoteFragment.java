package com.uzlov.myapplication.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.uzlov.myapplication.model.Note;
import com.uzlov.myapplication.R;
import com.uzlov.myapplication.repositories.note.NoteFirestoreCallbacks;
import com.uzlov.myapplication.repositories.note.NoteRepository;
import com.uzlov.myapplication.repositories.note.NoteRepositoryImpl;

import java.util.UUID;

public class AddNewNoteFragment extends Fragment implements NoteFirestoreCallbacks {

    private static String ARG_INDEX = "index";
    private TextInputEditText tvTitle;
    private TextInputEditText tvNameAutor;
    private TextInputEditText tvDescription;
    private MaterialButton btnSaveNote;
    private MaterialButton btnDatePicker;
    private final NoteRepository repository = new NoteRepositoryImpl(this);

    public static AddNewNoteFragment newInstance() {

        AddNewNoteFragment f = new AddNewNoteFragment();    // создание
        // Передача параметра
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_note_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tie_title);
        tvNameAutor = view.findViewById(R.id.tie_name_autour);
        tvDescription = view.findViewById(R.id.tie_description);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);
        btnDatePicker = view.findViewById(R.id.btnCallTimePicker);

        btnSaveNote.setOnClickListener(v ->{
            Note note = new Note(UUID.randomUUID().toString(),
                    tvTitle.getText().toString(),
                    tvDescription.getText().toString(),
                    btnDatePicker.getText().toString(),
                    tvNameAutor.getText().toString());

            Bundle result = new Bundle();
            result.putParcelable(ARG_INDEX, note);

            final String name = note.getName();
            final String description = note.getDescription();
            final String author = note.getAuthor();
            final String datetime = note.getDateCreate();
            update(name, description, author, datetime);

            if (getActivity() != null) getActivity().onBackPressed();

        });
        btnDatePicker.setOnClickListener(v -> openTimePicker());
    }

    private void openTimePicker() {
        MaterialTimePicker picker =
                new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText(R.string.select_new_string)
                        .build();

        if (getActivity() != null) {
            picker.show(requireActivity().getSupportFragmentManager(), "datePicker");
        }

        picker.addOnPositiveButtonClickListener(v1 ->
                btnDatePicker.setText(String.format("%d:%d", picker.getHour(), picker.getMinute())));
    }

    private void update(@Nullable String title,
                        @Nullable String description,
                        @Nullable String author,
                        @Nullable String datetime) {
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) &&
                !TextUtils.isEmpty(author) && !TextUtils.isEmpty(datetime)) {
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
    public void onSuccess(@Nullable  String message) {
        showToastMessage(message);
    }

    @Override
    public void onError(@Nullable  String message) {
        showToastMessage(message);
    }
}
