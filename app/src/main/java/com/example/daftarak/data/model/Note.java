package com.example.daftarak.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;

import kotlinx.serialization.Serializable;

@Serializable
@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(
                entity = Notebook.class,
                parentColumns = "id",
                childColumns = "notebook_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Note implements java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    @NonNull
    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "notebook_id", index = true)
    private int notebookId;

    public Note(@NonNull String title, String body, long createdAt, int notebookId) {
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.notebookId = notebookId;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {  // Room will set this automatically
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(int notebookId) {
        this.notebookId = notebookId;
    }
}
