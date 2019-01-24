package com.example.timothyyirenkyi.journalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder>{

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

    }

    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        ImageView mediaImageView;


        // ViewHolder constructor
        public MediaViewHolder(View itemView) {
            super(itemView);

            mediaImageView = itemView.findViewById(R.id.media_item);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = mJournalEntries.get(getAdapterPosition()).getId();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
