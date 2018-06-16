package io.bigsoft.android.popcorntime.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import io.bigsoft.android.popcorntime.model.Movie;

public class DbUtils {

    /**
     * Adds a new movie to favorites including the title and poster path
     *
     * @param movie
     * @param context
     */
    public static void addFavorite(Movie movie, Context context) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
    }


    // removeFavorite to remove a movie from favorites list that takes int id as input and returns a boolean
    /**
     * Removes the record with the specified id
     *
     * @param id the Movie id to be removed
     * @param context
     */
    public static void removeFavorite(int id, Context context) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        context.getContentResolver().delete(uri.buildUpon().appendPath(Integer.toString(id)).build(), null, null);
    }

    // isFavorite function to check if movie is a favorite or not that takes id as input and returns boolean
    /**
     * Checks if movie with the specified id is in the favorites
     *
     * @param id the Movie id to be checked
     * @return True: if exists, False: if not
     */
    public static boolean isFavorite(int id, Context context) {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        if (context.getContentResolver().query(uri.buildUpon().appendPath(Integer.toString(id)).build()
        , null
        , null
        , null
        , null).getCount()>0){
            return true;
        } else {
            return false;
        }
    }
}
