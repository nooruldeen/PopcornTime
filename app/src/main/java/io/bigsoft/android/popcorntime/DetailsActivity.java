package io.bigsoft.android.popcorntime;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bigsoft.android.popcorntime.adapter.MoviesAdapter;
import io.bigsoft.android.popcorntime.model.Movie;

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

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        mCollapsingToolbarLayout.setTitleEnabled(false);

        String posterImageUrl;

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        if (intent != null){
            if (intent.hasExtra(MoviesAdapter.TITLE_INTENT_KEY)){
                mTitle.setText(intent.getStringExtra(MoviesAdapter.TITLE_INTENT_KEY));
                mReleaseDate.setText(intent.getStringExtra(MoviesAdapter.RELEASE_DATE_INTENT_KEY));
//                mPopularity.setText(Double.toString(intent.getDoubleExtra(MoviesAdapter.POPULARITY_INTENT_KEY,0.0)));
                mRatingNumber.setText(Double.toString(intent.getDoubleExtra(MoviesAdapter.VOTE_INTENT_KEY,0.0)));
                mOverview.setText(intent.getStringExtra(MoviesAdapter.OVERVIEW_INTENT_KEY));
                posterImageUrl = "https://image.tmdb.org/t/p/w500" +intent.getStringExtra(MoviesAdapter.POSTER_INTENT_KEY);

                Glide.with(this)
                        .load(posterImageUrl)
                        .into(mPoster);
            }
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
