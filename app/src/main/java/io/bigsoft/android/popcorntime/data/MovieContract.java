package io.bigsoft.android.popcorntime.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "io.bigsoft.android.popcorntime";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "favorite_movies" directory
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    public static final class MovieEntry implements BaseColumns{


        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_POSTER_PATH="posterPath";
        public static final String COLUMN_BACKDROP_PATH="backdropPath";
        public static final String COLUMN_VOTE_AVERAGE="voteAverage";
        public static final String COLUMN_RELEASE_DATE="releaseDate";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_VIDEO="video";
        public static final String COLUMN_VOTE_COUNT="voteCount";
        public static final String COLUMN_POPULARITY="popularity";
        public static final String COLUMN_ORIGINAL_LANGUAGE="originalLanguage";
        public static final String COLUMN_ORIGINAL_TITLE="originalTitle";
        public static final String COLUMN_ADULT="adult";
    }

    private MovieContract() {
    }
}
