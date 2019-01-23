package com.example.timothyyirenkyi.journalapp.Data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


public class AddEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final JournalDatabase database;
    private final int mEntryId;

    public AddEntryViewModelFactory(JournalDatabase journalDatabase, int entryId) {
        database = journalDatabase;
        mEntryId = entryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddEntryViewModel(database, mEntryId);
    }
}
