package com.example.timothyyirenkyi.journalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<JournalEntry>> journal;

    public MainViewModel(@NonNull Application application) {
        super(application);
        JournalDatabase database = JournalDatabase.getsInstance(this.getApplication());
        journal = database.journalDao().loadAllEntries();
    }

    public LiveData<List<JournalEntry>> getJournal() {
        return journal;
    }
}
