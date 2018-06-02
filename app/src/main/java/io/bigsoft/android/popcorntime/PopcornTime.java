package io.bigsoft.android.popcorntime;

import android.app.Activity;
import android.app.Application;

import io.bigsoft.android.popcorntime.api.ApiUtilities;
import io.bigsoft.android.popcorntime.api.TMDBService;

public class PopcornTime extends Application {

    public static PopcornTime get(Activity activity) {
        return (PopcornTime) activity.getApplication();
    }

    private TMDBService mService;

    @Override
    public void onCreate() {
        super.onCreate();

        mService = ApiUtilities.getTMDBService();
    }

    public TMDBService getTMDBService() {  return mService;  }
}
