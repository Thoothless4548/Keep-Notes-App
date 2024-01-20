package com.anagha.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.R;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private ListView listView;
    private NotesDbHelper dbHelper; //declares an instance of the NotesDbHelper class to interact with the SQLite database.

    @Override
    protected void onCreate(Bundle savedInstanceState) {//activity is being created.
        super.onCreate(savedInstanceState); //calls the parent class's onCreate() method.
        setContentView(R.layout.activity_main); // sets the content view for the activity

        dbHelper = new NotesDbHelper(this);//initializes the dbHelper object by creating an instance of the NotesDbHelper class.

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList);
        listView = findViewById(R.id.listView);
        listView.setAdapter(noteAdapter); //sets the noteAdapter as the adapter for the listView.

        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog();
            }
        });

        // Load existing notes from the database
        loadNotes();
    }

    private void showAddNoteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        dialogBuilder.setView(dialogView);

        final EditText titleEditText = dialogView.findViewById(R.id.titleEditText);

        dialogBuilder.setTitle("Add Note");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = titleEditText.getText().toString();
                String description = ""; // Empty description initially
                long timeStamp = System.currentTimeMillis();
                Note note = new Note(title, description, timeStamp);
                saveNoteToDatabase(note); // Save the note to the database
                noteList.add(note);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled, do nothing
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void saveNoteToDatabase(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = note.toContentValues();
        long newRowId = db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);
    }

    private void loadNotes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                NoteContract.NoteEntry._ID,
                NoteContract.NoteEntry.COLUMN_TITLE,
                NoteContract.NoteEntry.COLUMN_DESCRIPTION,
                NoteContract.NoteEntry.COLUMN_TIMESTAMP
        };

        Cursor cursor = db.query(
                NoteContract.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        noteList.clear();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_DESCRIPTION));
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_TIMESTAMP));
            Note note = new Note(title, description, timestamp);
            note.setId(id);
            noteList.add(note);
        }

        cursor.close();
        noteAdapter.notifyDataSetChanged();
    }

    // Other methods for editing and deleting notes
    // ...
    public void editNoteDialog(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        dialogBuilder.setView(dialogView);

        final EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        titleEditText.setText(noteList.get(position).getTitle());

        dialogBuilder.setTitle("Edit Note");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = titleEditText.getText().toString();
                long timeStamp = System.currentTimeMillis();
                Note note = noteList.get(position);
                note.setTitle(title);
                note.setTimeStamp(timeStamp);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled, do nothing
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void deleteNoteDialog(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Delete Note");
        dialogBuilder.setMessage("Are you sure you want to delete this note?");
        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                noteList.remove(position);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled, do nothing
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }


}