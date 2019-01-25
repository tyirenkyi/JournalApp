package com.example.timothyyirenkyi.journalapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.example.timothyyirenkyi.journalapp.Data.AddEntryViewModel;
import com.example.timothyyirenkyi.journalapp.Data.AddEntryViewModelFactory;
import com.example.timothyyirenkyi.journalapp.Data.JournalDatabase;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEntryActivity extends AppCompatActivity {

    // Date formatter
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat2;
    // Constant for date format
    private static final String DATE_FORMAT = "MMM, hh:mm a";

    private static final String DATE_FORMAT1 = "MM dd, yyyy";

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;

    public static final String MEDIA_TASK = "mediaTask";

    EditText titleEditText;
    EditText descEditText;
    ImageView imageView;

    private int mEntryId = DEFAULT_TASK_ID;

    private JournalDatabase journalDatabase;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        initViews();

        dateFormat2 = new SimpleDateFormat(DATE_FORMAT1, Locale.getDefault());

        journalDatabase = JournalDatabase.getsInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mEntryId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        intent = getIntent();
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
        } else if(intent != null && intent.hasExtra(MEDIA_TASK)) {

            String stringUri = intent.getStringExtra(MEDIA_TASK);
            Uri uri = Uri.parse(stringUri);
            RequestManager requestManager = Glide.with(imageView);
            RequestBuilder requestBuilder = requestManager.load(uri);
            requestBuilder.into(imageView);

            Log.v("Add", stringUri);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(INSTANCE_TASK_ID, mEntryId);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                onSaveButtonClicked();
                return true;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    public void initViews() {
        titleEditText = findViewById(R.id.title_editText);
        descEditText = findViewById(R.id.desc_editText);
        imageView = findViewById(R.id.image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Date date = new Date();
        String dateNow = dateFormat.format(date);
        getSupportActionBar().setTitle(dateNow);

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
        String simpleDate = dateFormat2.format(date);

        if (intent != null && intent.hasExtra(MEDIA_TASK)) {
            String stringUri = intent.getStringExtra(MEDIA_TASK);
            Uri mediaUri = Uri.parse(stringUri);
            Log.v("AddEntry", stringUri);
            final JournalEntry journalEntry = new JournalEntry(description, title, date, simpleDate, mediaUri);
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
        } else {
            final JournalEntry journalEntry = new JournalEntry(description, title, date, simpleDate, null);
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
}
