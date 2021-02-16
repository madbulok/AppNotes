package com.uzlov.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class NoteFragment extends Fragment {

    private TextView tvName;
    private TextView tvAuthor;
    private EditText etDescription;


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
    }

    private void initView(View view){
        tvName = view.findViewById(R.id.textview_name);
        tvAuthor = view.findViewById(R.id.textView_author);
        etDescription = view.findViewById(R.id.etDescription);
    }
}