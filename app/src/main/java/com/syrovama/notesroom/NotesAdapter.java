package com.syrovama.notesroom;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Holder> {
    private static final String TAG = "MyAdapter";
    private List<Note> mNotes;
    private float mTextSize;
    private ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    NotesAdapter(List<Note> dataset, Context context) {
        mNotes = dataset;
        setTextSize(context);
    }

    public void setTextSize(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int resourceId = sharedPreferences.getInt(SettingsActivity.PREF_TEXT_SIZE, R.dimen.medium_text);
        mTextSize = context.getResources().getDimension(resourceId)/context.getResources().getDisplayMetrics().density;
        Log.d(TAG, "Text size " + mTextSize);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup recycler, int i) {
        LayoutInflater inflater = LayoutInflater.from(recycler.getContext());
        return new Holder(inflater.inflate(R.layout.recycler_item, recycler, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.bind(mNotes.get(i));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mTitleTextView;
        private final TextView mContentTextView;


        Holder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mContentTextView = itemView.findViewById(R.id.content_text_view);
        }
        void bind(Note note) {
            mTitleTextView.setText(note.getTitle());
            mTitleTextView.setTextSize(mTextSize);
            mContentTextView.setText(note.getContent());
            mTitleTextView.setTextSize(mTextSize);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Holder got the click event");
            mClickListener.onItemClick(getAdapterPosition(), view);
        }

    }
}


