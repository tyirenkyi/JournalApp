package com.example.timothyyirenkyi.journalapp.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity (tableName = "journal")
public class JournalEntry {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private String title;
    private Date updatedAt;
    private String simpleDate;

    @Ignore
    public JournalEntry(String description, String title, Date updatedAt, String simpleDate) {
        this.description = description;
        this.title = title;
        this.updatedAt = updatedAt;
        this.simpleDate = simpleDate;
    }

    public JournalEntry(int id, String description, String title, Date updatedAt, String simpleDate) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.updatedAt = updatedAt;
        this.simpleDate = simpleDate;
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

    public void setSimpleDate(String simpleDate) {
        this.simpleDate = simpleDate;
    }

    public String getSimpleDate() {
        return simpleDate;
    }
}
