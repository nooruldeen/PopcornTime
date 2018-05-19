package io.bigsoft.android.popcorntime;

import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.bigsoft.android.popcorntime.adapter.EndlessScrollListener;
import io.bigsoft.android.popcorntime.adapter.MoviesAdapter;
import io.bigsoft.android.popcorntime.api.ApiUtilities;
import io.bigsoft.android.popcorntime.api.TMDBService;
import io.bigsoft.android.popcorntime.model.Movie;
import io.bigsoft.android.popcorntime.model.MovieResponses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private MoviesAdapter mAdapter;
    private TMDBService mService;
    private String mPosition;
    private EndlessScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private AppCompatActivity activity = MainActivity.this;
    public static final String LOG_TAG = MoviesAdapter.class.getName();

    //(1) Create a key String called LIFECYCLE_CALLBACKS_TEXT_KEY
    private static final String LIFECYCLE_CALLBACK_TEXT_KEY = "callback";
    private static final String SEARCH_QUERY_URL_EXTRA = "sortType";


    //(2) Override onSaveInstanceState
//(3) Call super.onSaveInstanceState
//(4) Call logAndAppend with the ON_SAVE_INSTANCE_STATE String
//(5) Put the text from the TextView in the outState bundle
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logAndAppend("ON_SAVE_INSTANCE_STATE");
    }

    private void logAndAppend(String on_save_instance_state) {
        Log.d(TAG,"Lifecycle log: "+on_save_instance_state);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState !=null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACK_TEXT_KEY)) {
                String allPreviousLifecycleCallbacks = savedInstanceState.getString(LIFECYCLE_CALLBACK_TEXT_KEY);
            }
        }


        RecyclerView mRecyclerView;

        mService = ApiUtilities.getTMDBService();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_contents);

        mAdapter = new MoviesAdapter(this, new ArrayList<Movie>(0), new MoviesAdapter.PostMoviesListener() {

            @Override
            public void onPostClick(long id) {
                Toast.makeText(MainActivity.this, "Post id is" + id, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        loadMovies(1);
        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadMovies(page);
                return false;
            }
        };
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
