package com.example.timothyyirenkyi.journalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.timothyyirenkyi.journalapp.Fragments.CalendarFragment;
import com.example.timothyyirenkyi.journalapp.Fragments.MainFragment;
import com.example.timothyyirenkyi.journalapp.Fragments.MediaFragment;


public class MainActivity extends AppCompatActivity {
    MainFragment mainFragment;
    MediaFragment mediaFragment;
    CalendarFragment calendarFragment;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (savedInstanceState == null) {
            mainFragment = MainFragment.newInstance();
            mediaFragment = MediaFragment.newInstance();
            calendarFragment = CalendarFragment.newInstance();
        }
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_frame_layout, mediaFragment, "MediaFragment")
                .detach(mediaFragment)
                .add(R.id.fragment_frame_layout, calendarFragment, "CalendarFragment")
                .detach(calendarFragment)
                .add(R.id.fragment_frame_layout, mainFragment, "MainFragment")
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.journal_button:
                        fragmentManager.beginTransaction()
                                .detach(calendarFragment)
                                .detach(mediaFragment)
                                .attach(mainFragment)
                                .commit();
                        break;
                    case R.id.calendar_button:
                        fragmentManager.beginTransaction()
                                .detach(mainFragment)
                                .detach(mediaFragment)
                                .attach(calendarFragment)
                                .commit();
                        break;
//                    case R.id.media_button:
//                        fragmentManager.beginTransaction()
//                                .detach(mainFragment)
//                                .detach(calendarFragment)
//                                .attach(mediaFragment)
//                                .commit();
//                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
