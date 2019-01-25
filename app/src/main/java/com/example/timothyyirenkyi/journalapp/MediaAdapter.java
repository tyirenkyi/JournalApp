package com.example.timothyyirenkyi.journalapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder>{

    // Constant for date format
    private static final String DATE_FORMAT = "MMM dd, yyyy.EEE";

    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private Context mContext;

    private List<JournalEntry> mJournalEntries;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public MediaAdapter(Context context, ListItemClickListener clickListener) {
        mContext = context;
        mOnClickListener = clickListener;
    }


    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = mContext;
        int layoutIdForListItem = R.layout.media_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MediaViewHolder viewHolder = new MediaViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder mediaViewHolder, int i) {
        JournalEntry journalEntry = mJournalEntries.get(i);

        boolean isPhoto = journalEntry.getImageUri() != null;
        if (isPhoto) {

            Log.v("MediaAdapter", journalEntry.getImageUri().toString());
            Glide.with(mediaViewHolder.mediaImageView.getContext())
                    .load(journalEntry.getImageUri())
                    .into(mediaViewHolder.mediaImageView);
            mediaViewHolder.entryTitle.setText(journalEntry.getTitle());
            mediaViewHolder.entryDesc.setText(journalEntry.getDescription());
            mediaViewHolder.entryTime.setText(dateFormat.format(journalEntry.getUpdatedAt()));
        } else {
            Log.v("MediaAdapter", "" + isPhoto);
        }
    }

    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    public void setmJournalEntries(List<JournalEntry> mJournalEntries) {
        this.mJournalEntries = mJournalEntries;
        notifyDataSetChanged();
    }

    public List<JournalEntry> getmJournalEntries() {
        return mJournalEntries;
    }

    class MediaViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        ImageView mediaImageView;
        TextView entryTitle;
        TextView entryDesc;
        TextView entryTime;


        // ViewHolder constructor
        public MediaViewHolder(View itemView) {
            super(itemView);

            mediaImageView = itemView.findViewById(R.id.media_item);
            entryTime = itemView.findViewById(R.id.journal_item_time);
            entryDesc = itemView.findViewById(R.id.journal_item_desc);
            entryTitle = itemView.findViewById(R.id.journal_item_title);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = mJournalEntries.get(getAdapterPosition()).getId();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
