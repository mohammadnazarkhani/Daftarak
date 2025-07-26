package com.mnazar.daftarak.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mnazar.daftarak.data.dao.NoteDao;
import com.mnazar.daftarak.data.dao.NotebookDao;
import com.mnazar.daftarak.data.model.Note;
import com.mnazar.daftarak.data.model.Notebook;

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
            // افزودن یادداشت‌های نمونه به دفترچه
            db.noteDao().insertNote(new Note(
                    "برنامه‌ریزی روزانه",
                    "امروز باید لیست کارها را مرور کنم و اولویت‌ها را مشخص کنم.",
                    System.currentTimeMillis(),
                    (int) notebookId
            ));

            db.noteDao().insertNote(new Note(
                    "خریدهای خانه",
                    "نان، شیر، تخم‌مرغ، میوه و مواد شوینده را باید بخرم.",
                    System.currentTimeMillis(),
                    (int) notebookId
            ));

            db.noteDao().insertNote(new Note(
                    "مطالعه کتاب",
                    "ادامه‌ی فصل سوم از کتاب قدرت عادت را بخوانم.",
                    System.currentTimeMillis(),
                    (int) notebookId
            ));

        });
    }
}
