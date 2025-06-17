package com.example.daftarak.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.daftarak.data.model.Note;
import com.example.daftarak.data.model.Notebook;
import com.example.daftarak.data.dao.NoteDao;
import com.example.daftarak.data.dao.NotebookDao;

@Database(entities = {Notebook.class, Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public abstract NotebookDao notebookDao();
    public abstract NoteDao noteDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "daftarak_database"
                            )
                            .fallbackToDestructiveMigration() // optional: rebuild DB on schema changes
                            .build();
                }
            }
        }
        return instance;
    }
}
