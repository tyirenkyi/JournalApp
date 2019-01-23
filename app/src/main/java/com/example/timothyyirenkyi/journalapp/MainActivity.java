package com.example.timothyyirenkyi.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ListItemClickListener{
    private JournalAdapter journalAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    private JournalDatabase journalDatabase;

    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        journalDatabase = JournalDatabase.getsInstance(getApplicationContext());

        addButton = findViewById(R.id.add_floatingActionButton);
        recyclerView = findViewById(R.id.journal_list);
        emptyView = findViewById(R.id.empty_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyView.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                startActivity(intent);
            }
        });

        journalAdapter = new JournalAdapter(this, this);
        recyclerView.setAdapter(journalAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<JournalEntry> journalEntries = journalAdapter.getEntries();
                        journalDatabase.journalDao().deleteEntry(journalEntries.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar_button:
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getJournal().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                if (journalEntries == null || journalEntries.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    journalAdapter.setEntries(journalEntries);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Launch AddEntryActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
        intent.putExtra(AddEntryActivity.EXTRA_TASK_ID, clickedItemIndex);
        startActivity(intent);
    }
}
