package com.uzlov.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements OnChangeCurrentNote {
    private boolean isLandscape;
    private Note currentNote;
    public static final String ARG_INDEX = "index";
    private boolean isFistLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null && savedInstanceState.getParcelable(ARG_INDEX) != null){
            currentNote = savedInstanceState.getParcelable(ARG_INDEX);
            Log.e("MainActivity Create ", currentNote.getName());
            isFistLaunch = false;
        } else {
            currentNote = new Note("Первая");
            isFistLaunch = true;
        }

        if (isLandscape){
            showLandNote(currentNote);
        } else {
            showPortNote(currentNote);
        }
    }

    private void showLandNote(Note currentNote) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.notes_fragment, ListNotesFragment.newInstance(this, currentNote))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        if (!isFistLaunch){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.note_desc_container, NoteFragment.newInstance(currentNote))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        isFistLaunch = false;
    }

    private void showPortNote(Note currentNote) {
        Log.e("showPortNote", "showPortNote");
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container_fragments, ListNotesFragment.newInstance(this, currentNote))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
    }

    private void showPortNewNote(Note currentNote) {
        Log.e("showPortNote", "showPortNote");
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container_fragments, NoteFragment.newInstance(currentNote))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
    }

    @Override
    public void onBackPressed() {
        if (!isLandscape){
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragments, ListNotesFragment.newInstance(this, currentNote))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_INDEX, currentNote);
        Log.e("MainActivity, save", currentNote.getName());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getParcelable(ARG_INDEX) == null) return;
        currentNote = savedInstanceState.getParcelable(ARG_INDEX);
        Log.e("MainActivity, restore", currentNote.getName());
    }

    @Override
    public void newCurrentNote(Note note) {
        currentNote = note;
        if (isLandscape){
            showLandNote(currentNote);
        } else {
            showPortNewNote(currentNote);
        }

        Log.e("getFragments: ", String.valueOf(getSupportFragmentManager().getFragments().size()));
        Log.e("BackStackEntryCount: ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
    }
}