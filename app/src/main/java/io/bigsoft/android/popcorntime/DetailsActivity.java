package io.bigsoft.android.popcorntime;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bigsoft.android.popcorntime.adapter.MoviesAdapter;
import io.bigsoft.android.popcorntime.adapter.ReviewsAdapter;
import io.bigsoft.android.popcorntime.adapter.TrailersAdapter;
import io.bigsoft.android.popcorntime.api.TMDBService;
import io.bigsoft.android.popcorntime.model.Movie;
import io.bigsoft.android.popcorntime.model.Review;
import io.bigsoft.android.popcorntime.model.ReviewResponses;
import io.bigsoft.android.popcorntime.model.Trailer;
import io.bigsoft.android.popcorntime.model.TrailerResponses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_poster)
    ImageView mPoster;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;
    @BindView(R.id.tb_details_activity)
    Toolbar mToolbar;
    @BindView(R.id.tv_rating)
    TextView mRatingNumber;
    @BindView(R.id.tv_overview)
    TextView mOverview;
    @BindView(R.id.tb_collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.rv_trailer_details)
    RecyclerView mTrailers;
    @BindView(R.id.rv_reviews_details)
    RecyclerView mReviews;

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private TMDBService mService;

    private LinearLayoutManager trailersLayoutManager;
    private LinearLayoutManager reviewsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        populateMovie();
    }

    private void populateMovie(){

        String posterImageUrl;

        Intent intent = getIntent();

        if (intent != null){
            if (intent.hasExtra(MoviesAdapter.INTENT_BUNDLE_KEY)){
                Bundle bundle = intent.getBundleExtra(MoviesAdapter.INTENT_BUNDLE_KEY);
                Movie movie = (Movie) bundle.getSerializable(MoviesAdapter.BUNDLE_KEY);

                mTitle.setText(movie.getTitle());
                mReleaseDate.setText(movie.getReleaseDate());
                mRatingNumber.setText(Double.toString(movie.getVoteAverage()));
                mOverview.setText(movie.getOverview());
                posterImageUrl = "https://image.tmdb.org/t/p/w500" +movie.getPosterPath();

                Glide.with(this)
                        .load(posterImageUrl)
                        .into(mPoster);

                loadViews(BuildConfig.THEMOVIEDB_API_KEY, movie.getId());
            }
        }
    }

    private void loadViews(String apiKey, int id){

        mService = PopcornTime.get(this).getTMDBService();
        mCollapsingToolbarLayout.setTitleEnabled(false);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        trailersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailers.setLayoutManager(trailersLayoutManager);
        mTrailersAdapter = new TrailersAdapter(this, new ArrayList<Trailer>());
        mTrailers.setAdapter(mReviewsAdapter);

        Call<TrailerResponses> trailerResponsesCall = mService.getVideos(id, apiKey);

        trailerResponsesCall.enqueue(new Callback<TrailerResponses>() {
            @Override
            public void onResponse(Call<TrailerResponses> call, Response<TrailerResponses> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<TrailerResponses> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(DetailsActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();
            }
        });

        reviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviews.setLayoutManager(reviewsLayoutManager);
        mReviewsAdapter = new ReviewsAdapter(this, new ArrayList<Review>());
        mReviews.setAdapter(mReviewsAdapter);

        Call<ReviewResponses> reviewResponsesCall = mService.getReviews(id, apiKey);

        reviewResponsesCall.enqueue(new Callback<ReviewResponses>() {
            @Override
            public void onResponse(Call<ReviewResponses> call, Response<ReviewResponses> response) {

                Log.d("Retrofit2", "Retrofit response URL: "+call.request().url());

                if (response.isSuccessful()){
                    mReviewsAdapter.updateMovies(response.body().getResults());
                }else {
                    int statusCode  = response.code();

                    // handle request errors depending on status code
                    (Toast.makeText(DetailsActivity.this, "No reviews were found!", Toast.LENGTH_SHORT)).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponses> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
