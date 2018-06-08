package io.bigsoft.android.popcorntime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "favorites.db";

    // On database schema upgrade, increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public FavoritesDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold favorites data
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +    MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MovieContract.MovieEntry.COLUMN_ID + " INTEGER  NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VIDEO + " INTEGER, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT  NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL, " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT  NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_ADULT + " INTEGER, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT " +
                "); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
