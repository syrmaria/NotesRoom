package com.syrovama.notesroom;

import android.app.Application;

import android.arch.persistence.room.Room;

public class MyApplication extends Application {
    private NoteDatabase mNoteDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mNoteDatabase = Room.databaseBuilder(getApplicationContext(),
                NoteDatabase.class, "notes-database").allowMainThreadQueries().build();
    }

    public NoteDatabase getmNoteDatabase() {
        return mNoteDatabase;
    }
}
