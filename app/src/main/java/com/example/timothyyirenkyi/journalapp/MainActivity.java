package com.example.timothyyirenkyi.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.timothyyirenkyi.journalapp.Data.JournalDatabase;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;
import com.example.timothyyirenkyi.journalapp.Data.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ListItemClickListener{
    private JournalAdapter journalAdapter;
    private RecyclerView recyclerView;

    private JournalDatabase journalDatabase;

    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        journalDatabase = JournalDatabase.getsInstance(getApplicationContext());

        addButton = findViewById(R.id.add_floatingActionButton);
        recyclerView = findViewById(R.id.journal_list);

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

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getJournal().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                journalAdapter.setEntries(journalEntries);
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
