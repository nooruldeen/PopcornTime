package io.bigsoft.android.popcorntime.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bigsoft.android.popcorntime.DetailsActivity;
import io.bigsoft.android.popcorntime.R;
import io.bigsoft.android.popcorntime.model.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private Context mContext;
    private static List<Movie> mList;

    public static final String BUNDLE_KEY = "clicked_movie";
    public static final String INTENT_BUNDLE_KEY = "movie_bundle";

    public static List<Movie> getmList() {
        return mList;
    }

    public void setmList(List<Movie> mList) {
        MoviesAdapter.mList = mList;
    }

    public MoviesAdapter(Context context, List<Movie> moviesList, PostMoviesListener postMoviesListener) {
        this.mContext = context;
        this.mList = moviesList;
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_card, parent, false);

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapter.MoviesViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        String movieImageUrl = "https://image.tmdb.org/t/p/w185" + mList.get(position).getPosterPath();

        Glide.with(mContext)
                .load(movieImageUrl)
                .into(holder.movieCardImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateMovies(List<Movie> movies) {
        mList.addAll(movies);
        notifyDataSetChanged();
    }



    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_title)
        TextView title;
        @BindView(R.id.thumbnail)
        ImageView movieCardImage;

        public MoviesViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        Movie clickedDataItem = mList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(BUNDLE_KEY, clickedDataItem);
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        intent.putExtra(INTENT_BUNDLE_KEY, bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public interface PostMoviesListener{
        void onPostClick(long id);
    }

    public void ClearList(){
        mList.clear();
    }


}
