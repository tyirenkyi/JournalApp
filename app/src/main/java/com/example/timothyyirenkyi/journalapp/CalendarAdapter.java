package com.example.timothyyirenkyi.journalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    // Constant for date format
    private static final String DATE_FORMAT = "MMM dd, yyyy.EEE";

    private Context mContext;

    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    private List<JournalEntry> mJournalEntries;

    final private CalendarAdapter.ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public CalendarAdapter(Context context, ListItemClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = mContext;
        int layoutIdForListItem = R.layout.calendar_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        CalendarViewHolder viewHolder = new CalendarViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder calendarViewHolder, int i) {
        // Determine the values of the wanted data
        JournalEntry journalEntry = mJournalEntries.get(i);
        String description = journalEntry.getDescription();
        String title = journalEntry.getTitle();
        String time = dateFormat.format(journalEntry.getUpdatedAt());

        calendarViewHolder.entryDesc.setText(description);
        calendarViewHolder.entryTitle.setText(title);
        calendarViewHolder.entryTime.setText(time);
    }

    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }

        return mJournalEntries.size();
    }

    public void setJournalEntries(List<JournalEntry> journalEntries) {
        mJournalEntries = journalEntries;
        notifyDataSetChanged();
    }

    public List<JournalEntry> getJournalEntries() {
        return mJournalEntries;
    }

    /**
     * Cache of the views for the list item
     */
    class CalendarViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        TextView entryTitle;
        TextView entryDesc;
        TextView entryTime;

        public CalendarViewHolder(View itemView) {
            super(itemView);

            entryTitle = itemView.findViewById(R.id.journal_item_title);
            entryDesc = itemView.findViewById(R.id.journal_item_desc);
            entryTime = itemView.findViewById(R.id.journal_item_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = mJournalEntries.get(getAdapterPosition()).getId();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
