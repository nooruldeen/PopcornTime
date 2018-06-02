package io.bigsoft.android.popcorntime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bigsoft.android.popcorntime.R;
import io.bigsoft.android.popcorntime.model.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {


    private Context mContext;
    private static List<Review> mList;

    public ReviewsAdapter(Context mContext, List<Review> list) {
        this.mContext = mContext;
        mList = list;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_details,parent,false);

        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewsViewHolder holder, int position) {
        holder.reviewContent.setText(mList.get(position).getContent());
        holder.reviewAuthor.setText(mList.get(position).getAuthor());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_review_content)
        TextView reviewContent;
        @BindView(R.id.tv_review_author)
        TextView reviewAuthor;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateMovies(List<Review> reviews) {
        mList.addAll(reviews);
        notifyDataSetChanged();
    }
}
