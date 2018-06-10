package io.bigsoft.android.popcorntime;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bigsoft.android.popcorntime.adapter.MoviesAdapter;
import io.bigsoft.android.popcorntime.api.ApiUtilities;
import io.bigsoft.android.popcorntime.api.TMDBService;
import io.bigsoft.android.popcorntime.data.DbUtils;
import io.bigsoft.android.popcorntime.data.MovieContract;
import io.bigsoft.android.popcorntime.model.Movie;
import io.bigsoft.android.popcorntime.model.MovieResponses;
import io.bigsoft.android.popcorntime.adapter.EndlessRecyclerViewScrollListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_contents)
    RecyclerView mRecyclerView;

    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView.LayoutManager layoutManager;

    private String mSortType;
    private Context mContext;
    private MoviesAdapter mAdapter;
    private TMDBService mService;

    private static final int API_DEFAULT_PAGE =1;
    private static final String SCROLL_POSITION_KEY = "position";
    private static final String POPULAR_SORT_TYPE = "popular";
    private static final String TOP_RATED_SORT_TYPE = "top_rated";
    private static final String FAVORITES_TYPE = "favorites";
    private static final String PREVIOUS_TOTAL_ITEM_COUNT_KEY = "previous_total_item_count";
    private static final String LOADING_KEY = "loading";
    private static final String CURRENT_PAGE_KEY = "current_page";
    private static final String SORT_TYPE_KEY = "sort_type";
    private static final String MOVIES_LIST_KEY = "movies_list";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mService = PopcornTime.get(this).getTMDBService();
        mSortType = POPULAR_SORT_TYPE;
        mContext = this;

        setSupportActionBar(mToolbar);
        mAdapter = new MoviesAdapter(this, new ArrayList<Movie>(0), new MoviesAdapter.PostMoviesListener() {
            @Override
            public void onPostClick(long id) {
                Toast.makeText(MainActivity.this, "Post id is" + id, Toast.LENGTH_SHORT).show();
            }
        });

        layoutManager = getLayoutManager();

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        scrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, final int totalItemsCount, final RecyclerView view) {
                loadMovies(page, mSortType);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        if (savedInstanceState != null ) {
            if (savedInstanceState.containsKey(MOVIES_LIST_KEY)) {

                mSortType = savedInstanceState.getString(SORT_TYPE_KEY);

                ArrayList<Movie> list;
                list = savedInstanceState.getParcelableArrayList(MOVIES_LIST_KEY);
                mAdapter.setmList(list);
                mAdapter.notifyDataSetChanged();
                // Scroll to position
                ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(savedInstanceState.getInt(SCROLL_POSITION_KEY));
            }
        } else {
            loadMovies(API_DEFAULT_PAGE, mSortType);
        }


        if (savedInstanceState!=null) {
            if (savedInstanceState.containsKey(CURRENT_PAGE_KEY)) {
                scrollListener.setCurrentPage(savedInstanceState.getInt(CURRENT_PAGE_KEY));
                scrollListener.setLoading(savedInstanceState.getBoolean(LOADING_KEY));
                scrollListener.setPreviousTotalItemCount(savedInstanceState.getInt(PREVIOUS_TOTAL_ITEM_COUNT_KEY));
                savedInstanceState.clear();
            }
        }
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    private GridLayoutManager getLayoutManager(){
        RecyclerView.LayoutManager layoutManager = null;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(this,3);
        } else {
            layoutManager = new GridLayoutManager(this,2);
        }
        return (GridLayoutManager) layoutManager;
    }

    public void loadMovies(final int offset, String sortType) {

        Call<MovieResponses> movieResponsesCall = mService.getPopularMovies(BuildConfig.THEMOVIEDB_API_KEY, offset);

        switch (sortType){
            case POPULAR_SORT_TYPE:{
                movieResponsesCall = mService.getPopularMovies(BuildConfig.THEMOVIEDB_API_KEY, offset);
                break;
            }
            case TOP_RATED_SORT_TYPE:{
                movieResponsesCall = mService.getTopRatedMovies(BuildConfig.THEMOVIEDB_API_KEY, offset);
                break;
            }
            case FAVORITES_TYPE:{
                loadFavorites();
                break;
            }

        }

        if (sortType!=FAVORITES_TYPE) {
            movieResponsesCall.enqueue(new Callback<MovieResponses>() {
                @Override
                public void onResponse(Call<MovieResponses> call, Response<MovieResponses> response) {

                    if(response.isSuccessful()) {
                        mAdapter.updateMovies(response.body().getMovies());

                    }else {
                        int statusCode  = response.code();

                        // handle request errors depending on status code
                        (Toast.makeText(MainActivity.this, "Check your connection and try again!", Toast.LENGTH_SHORT)).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieResponses> call, Throwable t) {

                    // handle request errors depending on status code
                    (Toast.makeText(MainActivity.this, "Check your connection and try again!", Toast.LENGTH_SHORT)).show();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRecyclerView != null) {
            // Save layout state
            outState.putInt(SCROLL_POSITION_KEY, ((GridLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
            outState.putInt(CURRENT_PAGE_KEY, scrollListener.getCurrentPage());
            outState.putInt(PREVIOUS_TOTAL_ITEM_COUNT_KEY, scrollListener.getPreviousTotalItemCount());
            outState.putBoolean(LOADING_KEY, scrollListener.isLoading());
            outState.putString(SORT_TYPE_KEY, mSortType);
            outState.putParcelableArrayList(MOVIES_LIST_KEY, (ArrayList<? extends Parcelable>) mAdapter.getmList());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popcorn_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.popular_action:
                if (mSortType!=POPULAR_SORT_TYPE){
                    mSortType=POPULAR_SORT_TYPE;
                    mAdapter.ClearList();
                    mAdapter.notifyDataSetChanged();
                    scrollListener.resetState();
                    loadMovies(API_DEFAULT_PAGE, mSortType);
                }
                break;
            case R.id.top_rated_action:
                if (mSortType!=TOP_RATED_SORT_TYPE){
                    mSortType=TOP_RATED_SORT_TYPE;
                    mAdapter.ClearList();
                    mAdapter.notifyDataSetChanged();
                    scrollListener.resetState();
                    loadMovies(API_DEFAULT_PAGE, mSortType);
                }
                break;
            case R.id.favorites_action:
                if (mSortType!=FAVORITES_TYPE){
                    mSortType=FAVORITES_TYPE;
                    mAdapter.ClearList();
                    mAdapter.notifyDataSetChanged();
                    scrollListener.resetState();
                    loadMovies(API_DEFAULT_PAGE, mSortType);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSortType == FAVORITES_TYPE){
            loadFavorites();
        }
    }

    void loadFavorites(){
        new AsyncTask<Void, Void, Void>(){
            List<Movie> favoritesList = new ArrayList<>();
            @Override
            protected Void doInBackground(Void... params){
                favoritesList.addAll(DbUtils.getFavorites(mContext));
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                mAdapter.setmList(favoritesList);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
