package com.example.daftarak.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.daftarak.data.model.Notebook;

import java.util.List;

@Dao
public interface NotebookDao {

    @Insert
    long insertNotebook(Notebook notebook);

    @Update
    void updateNotebook(Notebook notebook);

    @Delete
    void deleteNotebook(Notebook notebook);

    @Query("SELECT * FROM notebooks ORDER BY created_at DESC")
    LiveData<List<Notebook>> getAllNotebooks(); // changed return type to LiveData

    @Query("SELECT * FROM notebooks WHERE id = :id LIMIT 1")
    LiveData<Notebook> getNotebookById(int id); // changed return type to LiveData
}
