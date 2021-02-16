package com.uzlov.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;



public class NoteFragment extends Fragment{

    private TextView tvDate;
    private TextView tvName;
    private TextView tvAuthor;
    private EditText etDescription;

    private OnChangeDateListener onChangeDateListener = new OnChangeDateListener() {
        @Override
        public void onChangeDate(String newDate) {
            tvDate.setText(newDate);
        }
    };

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

        tvDate.setOnClickListener(v -> {
            if (getActivity() != null){

                MaterialTimePicker picker =
                        new MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_24H)
                                .setTitleText(R.string.select_new_string)
                                .build();

                picker.show(getActivity().getSupportFragmentManager(), "datePicker");

                picker.addOnPositiveButtonClickListener(v1 -> {
                    onChangeDateListener.onChangeDate(picker.getHour()+":" + picker.getMinute());
                });
            }
        });
    }

    private void fillView() {
        etDescription.setText(index.getDescription());
        tvAuthor.setText(index.getAuthor());
        tvDate.setText(index.getDateCreate());
        tvName.setText(index.getName());
    }


//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        private OnChangeDateListener onChangeDateListener;
//
//        public DatePickerFragment(OnChangeDateListener onChangeDateListener) {
//            super();
//            this.onChangeDateListener = onChangeDateListener;
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            onChangeDateListener.onChangeDate(day + "." + (month+1) + "." + year);
//        }
//    }
}