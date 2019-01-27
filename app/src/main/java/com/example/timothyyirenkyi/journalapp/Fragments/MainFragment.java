package com.example.timothyyirenkyi.journalapp.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timothyyirenkyi.journalapp.AddEntryActivity;
import com.example.timothyyirenkyi.journalapp.AppExecutors;
import com.example.timothyyirenkyi.journalapp.Data.JournalDatabase;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;
import com.example.timothyyirenkyi.journalapp.Data.MainViewModel;
import com.example.timothyyirenkyi.journalapp.JournalAdapter;
import com.example.timothyyirenkyi.journalapp.R;

import java.util.List;

public class MainFragment extends Fragment implements JournalAdapter.ListItemClickListener {
    private JournalAdapter journalAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    private JournalDatabase journalDatabase;

    FloatingActionButton addButton;

    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        journalDatabase = JournalDatabase.getsInstance(getContext());

        addButton = view.findViewById(R.id.add_floatingActionButton);
        recyclerView = view.findViewById(R.id.journal_list);
        emptyView = view.findViewById(R.id.empty_view);

        emptyView.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEntryActivity.class);
                startActivity(intent);
            }
        });
        prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        Log.v(LOG_TAG, s);
                    }
                };
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener);


        journalAdapter = new JournalAdapter(getContext(), this);
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
        return view;
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getJournal().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                if (journalEntries == null || journalEntries.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    journalAdapter.setEntries(journalEntries);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Launch AddEntryActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(getContext(), AddEntryActivity.class);
        intent.putExtra(AddEntryActivity.EXTRA_TASK_ID, clickedItemIndex);
        startActivity(intent);
    }
}
