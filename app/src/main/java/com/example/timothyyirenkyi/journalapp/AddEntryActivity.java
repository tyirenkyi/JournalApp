package com.example.timothyyirenkyi.journalapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.timothyyirenkyi.journalapp.Data.AddEntryViewModel;
import com.example.timothyyirenkyi.journalapp.Data.AddEntryViewModelFactory;
import com.example.timothyyirenkyi.journalapp.Data.JournalDatabase;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;

import java.util.Date;

public class AddEntryActivity extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    EditText titleEditText;
    EditText descEditText;
    FloatingActionButton saveButton;

    private int mEntryId = DEFAULT_TASK_ID;

    private JournalDatabase journalDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        initViews();

        journalDatabase = JournalDatabase.getsInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mEntryId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            if (mEntryId == DEFAULT_TASK_ID) {
                // populate the UI
                // Use DEFAULT_TASK_ID as the default
                mEntryId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
                AddEntryViewModelFactory factory = new AddEntryViewModelFactory(journalDatabase, mEntryId);
                final AddEntryViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddEntryViewModel.class);
                viewModel.getJournal().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                        viewModel.getJournal().removeObserver(this);
                        populateUI(journalEntry);
                    }
                });
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(INSTANCE_TASK_ID, mEntryId);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void initViews() {
        titleEditText = findViewById(R.id.title_editText);
        descEditText = findViewById(R.id.desc_editText);
        saveButton = findViewById(R.id.floatingActionButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    private void populateUI(JournalEntry journalEntry) {
        if (journalEntry == null) {
            Log.v("AddEntryActivity", "hmmm");
            return;
        }
        titleEditText.setText(journalEntry.getTitle());
        descEditText.setText(journalEntry.getDescription());
    }
    public void onSaveButtonClicked() {
        String description = descEditText.getText().toString();
        String title = titleEditText.getText().toString();
        Date date = new Date();

        final JournalEntry journalEntry = new JournalEntry(description, title, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mEntryId == DEFAULT_TASK_ID) {
                    // insert new task
                    journalDatabase.journalDao().insertEntry(journalEntry);
                } else {
                    // update journal entry
                    journalEntry.setId(mEntryId);
                    journalDatabase.journalDao().updateEntry(journalEntry);
                }
                finish();
            }
        });
    }
}
