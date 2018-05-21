package io.bigsoft.android.popcorntime;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.bigsoft.android.popcorntime.Utils.Circle;
import io.bigsoft.android.popcorntime.adapter.MoviesAdapter;
import io.bigsoft.android.popcorntime.model.Movie;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private Circle mRating;
    private TextView mPopularity;
    private TextView mRatingNumber;
    private TextView mOverview;
    private TextView mGenreList;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mTitle = (TextView) findViewById(R.id.tv_title);
        mPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRatingNumber = (TextView) findViewById(R.id.tv_rating);
        mPopularity = (TextView) findViewById(R.id.tv_popularity);
        mOverview = (TextView) findViewById(R.id.tv_overview);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.tb_collapsing);

        String posterImageUrl;

        Intent intent = getIntent();

        if (intent != null){
            if (intent.hasExtra(MoviesAdapter.TITLE_INTENT_KEY)){
                mTitle.setText(intent.getStringExtra(MoviesAdapter.TITLE_INTENT_KEY));
                mReleaseDate.setText(intent.getStringExtra(MoviesAdapter.RELEASE_DATE_INTENT_KEY));
                mPopularity.setText(Double.toString(intent.getDoubleExtra(MoviesAdapter.POPULARITY_INTENT_KEY,0.0)));
                mRatingNumber.setText(Double.toString(intent.getDoubleExtra(MoviesAdapter.VOTE_INTENT_KEY,0.0)));
                mOverview.setText(intent.getStringExtra(MoviesAdapter.OVERVIEW_INTENT_KEY));
                posterImageUrl = "https://image.tmdb.org/t/p/w500" +intent.getStringExtra(MoviesAdapter.POSTER_INTENT_KEY);

                Glide.with(this)
                        .load(posterImageUrl)
                        .into(mPoster);
            }
        }
    }
}
