package com.example.timothyyirenkyi.journalapp.Fragments;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timothyyirenkyi.journalapp.AddEntryActivity;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;
import com.example.timothyyirenkyi.journalapp.Data.MainViewModel;
import com.example.timothyyirenkyi.journalapp.MediaAdapter;
import com.example.timothyyirenkyi.journalapp.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MediaFragment extends Fragment implements MediaAdapter.ListItemClickListener {

    private static final int RC_PHOTO_PICKER = 2;

    FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;
    private MediaAdapter adapter;

    private TextView emptyView;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public static MediaFragment newInstance() {
        return new MediaFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_fragment, container, false);

        recyclerView = view.findViewById(R.id.media_list);
        adapter = new MediaAdapter(getContext(), this);


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        emptyView = view.findViewById(R.id.empty_view);

        emptyView.setVisibility(View.GONE);

        recyclerView.setAdapter(adapter);
        floatingActionButton = view.findViewById(R.id.new_entry);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mediaIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                mediaIntent.setType("image/jpeg");
                mediaIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                mediaIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(mediaIntent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        setupViewModel();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // check for the freshest data
            getContext().getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
            String stringUri = selectedImageUri.toString();

            Intent intent = new Intent(getContext(), AddEntryActivity.class);
            intent.putExtra(AddEntryActivity.MEDIA_TASK, stringUri);
            startActivity(intent);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Launch AddEntryActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(getContext(), AddEntryActivity.class);
        intent.putExtra(AddEntryActivity.EXTRA_TASK_ID, clickedItemIndex);
        startActivity(intent);
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getJournal().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                if (journalEntries == null || journalEntries.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    adapter.setmJournalEntries(journalEntries);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }
}
