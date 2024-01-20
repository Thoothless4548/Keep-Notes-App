package com.anagha.notes;

import android.content.ContentValues;

public class Note {
    private long id;
    private String title;
    private String description;
    private long timeStamp;

    public Note() {
    }

    public Note(String title, String description, long timeStamp) {
        this.title = title;
        this.description = description;
        this.timeStamp = timeStamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_TITLE, title);
        values.put(NoteContract.NoteEntry.COLUMN_DESCRIPTION, description);
        values.put(NoteContract.NoteEntry.COLUMN_TIMESTAMP, timeStamp);
        return values;
    }
}
