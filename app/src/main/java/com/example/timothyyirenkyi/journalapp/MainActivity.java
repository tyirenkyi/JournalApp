package com.example.timothyyirenkyi.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.timothyyirenkyi.journalapp.Data.JournalDatabase;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;
import com.example.timothyyirenkyi.journalapp.Data.MainViewModel;
import com.example.timothyyirenkyi.journalapp.Fragments.CalendarFragment;
import com.example.timothyyirenkyi.journalapp.Fragments.MainFragment;
import com.example.timothyyirenkyi.journalapp.Fragments.MediaFragment;

import java.util.List;

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
                    case R.id.media_button:
                        fragmentManager.beginTransaction()
                                .detach(mainFragment)
                                .detach(calendarFragment)
                                .attach(mediaFragment)
                                .commit();
                        break;
                }
                return true;
            }
        });
    }

}
