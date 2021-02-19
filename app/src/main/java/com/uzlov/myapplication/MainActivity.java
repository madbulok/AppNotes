package com.uzlov.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements OnChangeCurrentNote {
    private boolean isLandscape;
    private Note currentNote;
    public static final String ARG_INDEX = "index";
    private boolean isFistLaunch;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        initView();

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

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // STUB!
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.settings_item:
                // STUB!
                return true;
            case R.id.about_item:
                // STUB!
                return true;
            case R.id.popular_item:
                // STUB!
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);

        return super.onCreateOptionsMenu(menu);
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
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container_fragments, ListNotesFragment.newInstance(this, currentNote))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
    }

    private void showPortNewNote(Note currentNote) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container_fragments, NoteFragment.newInstance(currentNote))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
    }

    @Override
    public void onBackPressed() {
        setTitleToolbar(getString(R.string.app_name));
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

    private void setTitleToolbar(String title){
        toolbar.setTitle(title);
    }

    @Override
    public void newCurrentNote(Note note) {
        currentNote = note;

        setTitleToolbar(note.getName());

        if (isLandscape){
            showLandNote(currentNote);
        } else {
            showPortNewNote(currentNote);
        }

        Log.e("getFragments: ", String.valueOf(getSupportFragmentManager().getFragments().size()));
        Log.e("BackStackEntryCount: ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
    }
}