package com.example.daftarak.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.daftarak.data.dao.NoteDao;
import com.example.daftarak.data.dao.NotebookDao;
import com.example.daftarak.data.model.Note;
import com.example.daftarak.data.model.Notebook;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Notebook.class, Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public abstract NotebookDao notebookDao();
    public abstract NoteDao noteDao();

    private static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "daftarak_database"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    seedInitialData(getInstance(context));
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }

    private static void seedInitialData(AppDatabase db) {
        databaseWriteExecutor.execute(() -> {
            // Create a notebook
            Notebook notebook = new Notebook(0, "MyNotebook", System.currentTimeMillis());
            long notebookId = db.notebookDao().insertNotebook(notebook);

            // Add notes to that notebook
            db.noteDao().insertNote(new Note(
                    "Complete Android project",
                    "Complete the Android project for my Mobile Development class.",
                    System.currentTimeMillis(),
                    (int) notebookId
            ));

            db.noteDao().insertNote(new Note(
                    "Send project to professor",
                    "Send the project to Professor Roshani.",
                    System.currentTimeMillis(),
                    (int) notebookId
            ));

            db.noteDao().insertNote(new Note(
                    "Study for exam",
                    "Study for the Mobile Development class exam.",
                    System.currentTimeMillis(),
                    (int) notebookId
            ));
        });
    }
}
