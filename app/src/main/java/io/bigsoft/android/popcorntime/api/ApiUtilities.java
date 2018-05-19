package io.bigsoft.android.popcorntime.api;

public class ApiUtilities {

    public static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/";

    public static TMDBService getTMDBService() {
        return TMDBClient.getClient(TMDB_BASE_URL).create(TMDBService.class);
    }
}
