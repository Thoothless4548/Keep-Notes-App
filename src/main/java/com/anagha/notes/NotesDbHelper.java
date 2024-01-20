package com.anagha.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1; //declares a constant variable DATABASE_NAME that represents the name of the database file

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NoteContract.NoteEntry.TABLE_NAME + " (" +
                    NoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NoteContract.NoteEntry.COLUMN_TITLE + " TEXT," +
                    NoteContract.NoteEntry.COLUMN_DESCRIPTION + " TEXT," +
                    NoteContract.NoteEntry.COLUMN_TIMESTAMP + " INTEGER)"; // statement creates a table named NoteEntry.TABLE_NAME with columns for the note's ID, title, description, and timestamp.

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME; //declares a constant variable TABLE_NAME that represents the name of the table for storing notes.

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

