package io.bigsoft.android.popcorntime.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.bigsoft.android.popcorntime.DetailActivity;
import io.bigsoft.android.popcorntime.R;
import io.bigsoft.android.popcorntime.model.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {


    private Context mContext;
    private List<Movie> mList;
    private PostMoviesListener mMoviesListener;

    public MoviesAdapter(Context context, List<Movie> moviesList, PostMoviesListener moviesLisener) {
        this.mContext = context;
        this.mList = moviesList;
        this.mMoviesListener = moviesLisener;
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_contents_card, parent, false);

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
        public TextView title;
        public ImageView movieCardImage;
        public MoviesViewHolder(View itemView) {

            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            movieCardImage = (ImageView) itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        Movie clickedDataItem = mList.get(position);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("movies", clickedDataItem );
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
}
