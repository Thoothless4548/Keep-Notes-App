package com.anagha.notes;

import android.provider.BaseColumns;

public final class NoteContract {

    private NoteContract() {
    }

    public static class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}

