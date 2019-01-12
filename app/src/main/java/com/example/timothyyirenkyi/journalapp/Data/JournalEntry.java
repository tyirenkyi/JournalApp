package com.example.timothyyirenkyi.journalapp.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "journal")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private String title;
    private Date updatedAt;

    @Ignore
    public JournalEntry(String description, String title, Date updatedAt) {
        this.description = description;
        this.title = title;
        this.updatedAt = updatedAt;
    }

    public JournalEntry(int id, String description, String title, Date updatedAt) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
