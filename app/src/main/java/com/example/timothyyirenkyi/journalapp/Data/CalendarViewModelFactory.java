package com.example.timothyyirenkyi.journalapp.Data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class CalendarViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final JournalDatabase database;
    private final String dateId;

    public CalendarViewModelFactory(JournalDatabase database, String dateId) {
        this.database = database;
        this.dateId = dateId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CalendarViewModel(database, dateId);
    }
}
