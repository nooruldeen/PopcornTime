package io.bigsoft.android.popcorntime;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.bigsoft.android.popcorntime.adapter.MoviesAdapter;
import io.bigsoft.android.popcorntime.api.ApiUtilities;
import io.bigsoft.android.popcorntime.api.TMDBService;
import io.bigsoft.android.popcorntime.model.Movie;
import io.bigsoft.android.popcorntime.model.MovieResponses;
import io.bigsoft.android.popcorntime.adapter.EndlessRecyclerViewScrollListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private MoviesAdapter mAdapter;
    private TMDBService mService;
    private RecyclerView mRecyclerView;
    private Parcelable mSavedState;
    private EndlessRecyclerViewScrollListener scrollListener;

    private static final String LIFECYCLE_CALLBACK_TEXT_KEY = "callback";
    private static final String SEARCH_QUERY_URL_EXTRA = "sortType";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRecyclerView != null) {
            // Save list state
            mSavedState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(LIFECYCLE_CALLBACK_TEXT_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
        }
        logAndAppend("ON_SAVE_INSTANCE_STATE: ");
    }

    private void logAndAppend(String on_save_instance_state) {
        Log.d(TAG,"Lifecycle log: "+on_save_instance_state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve list state and list/item positions
        if(savedInstanceState != null){
            mSavedState = savedInstanceState.getParcelable(LIFECYCLE_CALLBACK_TEXT_KEY);
            logAndAppend("ON_RESTORE_INSTANCE_STATE: ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSavedState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedState);
            logAndAppend("ON_RESUME_INSTANCE_STATE: ");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RecyclerView mRecyclerView;

        mService = ApiUtilities.getTMDBService();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_contents);

        mAdapter = new MoviesAdapter(this, new ArrayList<Movie>(0), new MoviesAdapter.PostMoviesListener() {

            @Override
            public void onPostClick(long id) {
                Toast.makeText(MainActivity.this, "Post id is" + id, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager layoutManager = null;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(this,5);
        } else {
            layoutManager = new GridLayoutManager(this,3);
        }
        mRecyclerView.setLayoutManager(layoutManager);

        if (mSavedState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedState);
            logAndAppend("ON_RESUME_INSTANCE_STATE: ");
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        loadMovies(1);

        //        Log.d(TAG, "onCreate: registering preference changed listener");
        scrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, final int totalItemsCount, final RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                loadNextDataFromApi(page);
                Log.d(TAG, "EndlessRecyclerViewScrollListener: Inside scrollListener. Page: "+page);

                loadMovies(page);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        // There is no need to use notifyDataSetChanged()
                        mAdapter.notifyDataSetChanged();
//                        mPopcornAdapter.notifyItemRangeInserted(totalItemsCount, totalItemsCount - 1);
                    }
                });

            }
        };

        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener);

    }

    public void loadMovies(int offset) {
        mService.getPopularMovies(BuildConfig.THEMOVIEDB_API_KEY, offset).enqueue(new Callback<MovieResponses>() {
            @Override
            public void onResponse(Call<MovieResponses> call, Response<MovieResponses> response) {

                if(response.isSuccessful()) {
                    mAdapter.updateMovies(response.body().getMovies());
                    Log.d("MainActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<MovieResponses> call, Throwable t) {
//                showErrorMessage();
                Log.d("MainActivity", "error loading from API");

            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popcorn_menu,menu);
        return true;
    }
}
