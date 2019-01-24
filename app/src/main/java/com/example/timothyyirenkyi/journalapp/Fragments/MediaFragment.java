package com.example.timothyyirenkyi.journalapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timothyyirenkyi.journalapp.R;

import static android.app.Activity.RESULT_OK;

public class MediaFragment extends Fragment {

    private static final int RC_PHOTO_PICKER = 2;

    FloatingActionButton floatingActionButton;

    public static MediaFragment newInstance() {
        return new MediaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_fragment, container, false);

        floatingActionButton = view.findViewById(R.id.new_entry);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent media_Intent = new Intent(Intent.ACTION_GET_CONTENT);
                media_Intent.setType("image/jpeg");
                media_Intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(media_Intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
        }
    }
}
