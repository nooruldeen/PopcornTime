package io.bigsoft.android.popcorntime.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.bigsoft.android.popcorntime.model.Movie;

public class DbUtils {

    /**
     * Query the db and get all favorite movies
     *
     * @return Cursor containing the list of favorite movies
     */


    public List<Movie> getAllFavorites(SQLiteDatabase db){
        String[] columns = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_ADULT

        };
        String sortOrder = MovieContract.MovieEntry._ID + " ASC";
        List<Movie> favoriteList = new ArrayList<>();
        Cursor cursor = getFavorites(db, sortOrder);

        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)));
                favoriteList.add(movie);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }

    public static Cursor getFavorites(SQLiteDatabase db, String orderBy) {
        return db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                orderBy
        );
    }

    /**
     * Adds a new movie to favorites including the title and poster path
     *
     * @param movie
     * @return id of new record added
     */
    public static long addFavorite(Movie movie, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getOriginalTitle());
        return db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
    }


    // removeFavorite to remove a movie from favorites list that takes int id as input and returns a boolean
    /**
     * Removes the record with the specified id
     *
     * @param id the Movie id to be removed
     * @return True: if removed successfully, False: if failed
     */
    public static boolean removeFavorite(int id, SQLiteDatabase db) {
        return db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_ID + "=" + id, null) > 0;
    }

    // isFavorite function to check if movie is a favorite or not that takes id as input and returns boolean
    /**
     * Checks if movie with the specified id is in the favorites
     *
     * @param id the Movie id to be removed
     * @return True: if removed successfully, False: if failed
     */
    public static boolean isFavorite(int id, SQLiteDatabase db) {
        if (db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                MovieContract.MovieEntry.COLUMN_ID+" = "+id,
                null,
                null,
                null,
                null
        ).getCount()>0){
            return true;
        }else {
            return false;
        }
    }
}
