package io.bigsoft.android.popcorntime;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.bigsoft.android.popcorntime.api.ApiUtilities;
import io.bigsoft.android.popcorntime.api.TMDBService;
import io.bigsoft.android.popcorntime.data.FavoritesDbHelper;

public class PopcornTime extends Application {

    public static PopcornTime get(Activity activity) {
        return (PopcornTime) activity.getApplication();
    }

    private TMDBService mService;
    private FavoritesDbHelper mDbHelper;
    private SQLiteDatabase writableDb;

    @Override
    public void onCreate() {
        super.onCreate();
        mDbHelper = new FavoritesDbHelper(this);
        mService = ApiUtilities.getTMDBService();
        mDbHelper = new FavoritesDbHelper(this);
        writableDb = mDbHelper.getWritableDatabase();
    }

    public TMDBService getTMDBService() {  return mService;  }

    public SQLiteDatabase getWritableFavorites() {
        return writableDb;
    }
}
