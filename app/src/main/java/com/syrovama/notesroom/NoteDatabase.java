package com.syrovama.notesroom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDAO getNoteDAO();
}
