package com.example.timothyyirenkyi.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.timothyyirenkyi.journalapp.Data.CalendarViewModel;
import com.example.timothyyirenkyi.journalapp.Data.CalendarViewModelFactory;
import com.example.timothyyirenkyi.journalapp.Data.JournalDatabase;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;
import com.example.timothyyirenkyi.journalapp.Data.MainViewModel;

import java.util.List;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.ListItemClickListener{

    CalendarView calendarView;

    CalendarAdapter calendarAdapter;
    RecyclerView recyclerView;
    JournalDatabase database;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendar);

        recyclerView = findViewById(R.id.calendar_list);

        calendarAdapter = new CalendarAdapter(this, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(calendarAdapter);

        emptyView = findViewById(R.id.empty_view);

        emptyView.setVisibility(View.GONE);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String trueMonth;
                switch (month) {
                    case 0:
                       trueMonth = "01";
                       break;
                    case 1:
                        trueMonth = "02";
                        break;
                    case 2:
                        trueMonth = "03";
                        break;
                    case 3:
                        trueMonth = "04";
                        break;
                    case 4:
                        trueMonth = "05";
                        break;
                    case 5:
                        trueMonth = "06";
                        break;
                    case 6:
                        trueMonth = "07";
                        break;
                    case 7:
                        trueMonth = "08";
                        break;
                    case 8:
                        trueMonth = "09";
                        break;
                    case 9:
                        trueMonth = "10";
                        break;
                    case 10:
                        trueMonth = "11";
                        break;
                    case 11:
                        trueMonth = "12";
                        break;
                    default:
                        trueMonth = "01";
                }

                StringBuilder date = new StringBuilder(trueMonth);
                date.append(" ");
                date.append(String.valueOf(day));
                date.append(", ");
                date.append(String.valueOf(year));

                database = JournalDatabase.getsInstance(getApplicationContext());

                String dateId = String.valueOf(date);

                CalendarViewModelFactory factory = new CalendarViewModelFactory(database, dateId);
                final CalendarViewModel viewModel = ViewModelProviders.of(CalendarActivity.this, factory).get(CalendarViewModel.class);
                viewModel.getCalendarList(database, dateId);
                viewModel.getJournal().observe(CalendarActivity.this, new Observer<List<JournalEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                        Log.v("CalendarActivity", "dop");
                        if (journalEntries == null || journalEntries.isEmpty()) {
                            emptyView.setVisibility(View.VISIBLE);
                            calendarAdapter.setJournalEntries(journalEntries);
                            Log.v("CalendarActivity", "boop");
                        } else {
                            emptyView.setVisibility(View.GONE);
                            calendarAdapter.setJournalEntries(journalEntries);
                            Log.v("CalendarActivity", "kop");
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Launch AddEntryActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(CalendarActivity.this, AddEntryActivity.class);
        intent.putExtra(AddEntryActivity.EXTRA_TASK_ID, clickedItemIndex);
        startActivity(intent);
    }
}
