package com.example.timothyyirenkyi.journalapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timothyyirenkyi.journalapp.Data.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "MMM dd, yyyy.EEE";

    private Context mContext;

    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    private List<JournalEntry> mJournalEntries;

    final private ListItemClickListener mOnClickListener;

    SharedPreferences.OnSharedPreferenceChangeListener prefListener;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public JournalAdapter(Context context, ListItemClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }



    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = mContext;
        int layoutIdForListItem = R.layout.journal_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        JournalViewHolder viewHolder = new JournalViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final JournalViewHolder journalViewHolder, int i) {
        // Determine the values of the wanted data
        JournalEntry journalEntry = mJournalEntries.get(i);
        String description = journalEntry.getDescription();
        String title = journalEntry.getTitle();
        String updatedAt = dateFormat.format(journalEntry.getUpdatedAt());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        Log.v("JournalAdapter", s);
                        String fontFamilyListPref = sharedPreferences.getString("font_list", "1");
                        Typeface typeface;
                        final JournalViewHolder mJournalViewHolder = journalViewHolder;
                        switch (fontFamilyListPref) {
                            case "1":
                                typeface = Typeface.createFromAsset(mContext.getAssets(), "font/raleway.ttf");
                                mJournalViewHolder.entryDesc.setTypeface(typeface);
                                mJournalViewHolder.entryTitle.setTypeface(typeface);
                                mJournalViewHolder.entryTime.setTypeface(typeface);
                                Log.v("JournalAdapter", "raleway");
                                break;
                            case "2":
                                typeface = Typeface.createFromAsset(mContext.getAssets(), "font/nunito.ttf");
                                mJournalViewHolder.entryDesc.setTypeface(typeface);
                                mJournalViewHolder.entryTitle.setTypeface(typeface);
                                mJournalViewHolder.entryTime.setTypeface(typeface);
                                break;
                            case "3":
                                typeface = Typeface.createFromAsset(mContext.getAssets(), "font/roboto.ttf");
                                mJournalViewHolder.entryDesc.setTypeface(typeface);
                                mJournalViewHolder.entryTitle.setTypeface(typeface);
                                mJournalViewHolder.entryTime.setTypeface(typeface);
                                break;
                            case "4":
                                typeface = get("font/work_sans.ttf", mContext);
                                mJournalViewHolder.entryDesc.setTypeface(typeface);
                                mJournalViewHolder.entryTitle.setTypeface(typeface);
                                mJournalViewHolder.entryTime.setTypeface(typeface);
                                break;
                            case "5":
                                typeface = get("font/noto_serif.ttf", mContext);
                                mJournalViewHolder.entryDesc.setTypeface(typeface);
                                mJournalViewHolder.entryTitle.setTypeface(typeface);
                                mJournalViewHolder.entryTime.setTypeface(typeface);
                        }
                    }
                };
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener);


        // Set values
        journalViewHolder.entryDesc.setText(description);
        journalViewHolder.entryTitle.setText(title);
        journalViewHolder.entryTime.setText(updatedAt);
    }


    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    public void setEntries(List<JournalEntry> journalEntries) {
        mJournalEntries = journalEntries;
        notifyDataSetChanged();
    }

    public List<JournalEntry> getEntries() {
        return mJournalEntries;
    }


    /**
     * Cache of the children views for a list item.
     */
    class JournalViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        TextView entryTitle;
        TextView entryDesc;
        TextView entryTime;

        // ViewHolder constructor
        public JournalViewHolder(View itemView) {
            super(itemView);

            entryTime = itemView.findViewById(R.id.journal_item_time);
            entryDesc = itemView.findViewById(R.id.journal_item_desc);
            entryTitle = itemView.findViewById(R.id.journal_item_title);
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//
//            SharedPreferences.OnSharedPreferenceChangeListener prefListener =
//                    new SharedPreferences.OnSharedPreferenceChangeListener() {
//                        @Override
//                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//                            Log.v("JournalAdapter", s);
//                        }
//                    };
//            sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener);


            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            entryTitle.setText(String.valueOf(listIndex));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = mJournalEntries.get(getAdapterPosition()).getId();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }


    private Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e)  {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}
