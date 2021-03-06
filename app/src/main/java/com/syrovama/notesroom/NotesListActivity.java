package com.syrovama.notesroom;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {
    private static final String TAG = "MyNotesListActivity";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_DELETED = "isDeleted";
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_OPEN = 2;
    public static final int REQUEST_SETTINGS = 3;
    private List<Note> mNotes;
    private NotesAdapter mAdapter;
    private int mSelectedPosition;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        initRecycler();
        Log.d(TAG, "onCreate Recycler initiated");
    }

    private void initRecycler() {
        Log.d(TAG, "initRecycler");
        mRecyclerView = findViewById(R.id.notes_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mNotes = ((MyApplication)getApplication()).getmNoteDatabase().getNoteDAO().getAll();
        mAdapter = new NotesAdapter(mNotes, this);
        mAdapter.setTextSize(this);
        mAdapter.setOnItemClickListener(new NotesAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                openNoteForUpdate(position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_note_menu_item:
                Intent addNoteIntent = new Intent(this, AddNoteActivity.class);
                startActivityForResult(addNoteIntent, REQUEST_ADD);
                return true;
            case R.id.settings_menu_item:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, REQUEST_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (data == null) {
            if (requestCode == REQUEST_SETTINGS) {
                Log.d(TAG, "Got new setting");
                initRecycler();
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_OPEN:
                if (data.getBooleanExtra(EXTRA_DELETED, false)) {
                    mNotes.remove(mSelectedPosition);
                    Log.d(TAG, "Note deleted");
                } else {
                    mNotes.set(mSelectedPosition, getNoteFromIntent(data));
                    Log.d(TAG, "Note updated");
                }
                break;
            case REQUEST_ADD:
                mNotes.add(getNoteFromIntent(data));
                Log.d(TAG, "Note added");
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void openNoteForUpdate(int position) {
        Log.d(TAG, "openNote called");
        mSelectedPosition = position;
        Note note = mNotes.get(position);
        Log.d(TAG, "Open note " + note.toString());
        startActivityForResult(getUpdateNoteIntent(note), REQUEST_OPEN);
    }

    public Intent getUpdateNoteIntent(Note note) {
        Intent intent = new Intent(this, UpdateNoteActivity.class);
        intent.putExtra(EXTRA_ID, note.getId());
        intent.putExtra(EXTRA_TITLE, note.getTitle());
        intent.putExtra(EXTRA_CONTENT, note.getContent());
        return intent;
    }

    private Note getNoteFromIntent(Intent intent) {
        Note note = new Note();
        note.setId(intent.getLongExtra(EXTRA_ID, 0));
        note.setTitle(intent.getStringExtra(EXTRA_TITLE));
        note.setContent(intent.getStringExtra(EXTRA_CONTENT));
        Log.d(TAG, "Got note " + note.toString());
        return note;
    }
}
