package com.uzlov.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.uzlov.myapplication.Note;
import com.uzlov.myapplication.R;
import com.uzlov.myapplication.interfaces.OnSaveListener;

public class AddNewNoteFragment extends Fragment {

    private static String ARG_INDEX = "index";
    private TextInputEditText tvTitle;
    private TextInputEditText tvNameAutor;
    private TextInputEditText tvDescription;
    private MaterialButton btnSaveNote;
    private OnSaveListener saveListener;
    private MaterialButton btnDatePicker;

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
            Note note = new Note(tvTitle.getText().toString(),
                    tvDescription.getText().toString(),
                    btnDatePicker.getText().toString(),
                    tvNameAutor.getText().toString()
            );

            Bundle result = new Bundle();
            result.putParcelable(ARG_INDEX, note);

            getParentFragmentManager().setFragmentResult(getString(R.string.key_save), result);

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


}
