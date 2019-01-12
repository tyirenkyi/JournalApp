package com.example.timothyyirenkyi.journalapp.Data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.timothyyirenkyi.journalapp.Data.JournalDatabase;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;

public class AddEntryViewModel extends ViewModel {
    private LiveData<JournalEntry> journal;

    public AddEntryViewModel(JournalDatabase database, int entryId) {
        journal = database.journalDao().loadEntryById(entryId);
    }

    public LiveData<JournalEntry> getJournal() {
        return journal;
    }
}
