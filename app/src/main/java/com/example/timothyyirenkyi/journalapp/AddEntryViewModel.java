package com.example.timothyyirenkyi.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class AddEntryViewModel extends ViewModel {
    private LiveData<JournalEntry> journal;
    
    public AddEntryViewModel(JournalDatabase database, int entryId) {
        journal = database.journalDao().loadEntryById(entryId);
    }

    public LiveData<JournalEntry> getJournal() {
        return journal;
    }
}
