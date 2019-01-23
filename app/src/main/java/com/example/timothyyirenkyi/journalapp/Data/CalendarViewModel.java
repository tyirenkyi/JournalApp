package com.example.timothyyirenkyi.journalapp.Data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class CalendarViewModel extends ViewModel {
    private LiveData<List<JournalEntry>> journal;

    public CalendarViewModel(JournalDatabase database, String dateId) {
        journal = database.journalDao().loadEntryBySimpleDate(dateId);
    }

    public LiveData<List<JournalEntry>> getJournal() {
        return journal;
    }

    public void getCalendarList(JournalDatabase journalDatabase, String dateId) {
        journal = journalDatabase.journalDao().loadEntryBySimpleDate(dateId);
    }
}
