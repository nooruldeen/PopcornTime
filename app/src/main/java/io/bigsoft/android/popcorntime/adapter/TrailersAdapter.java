package io.bigsoft.android.popcorntime.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bigsoft.android.popcorntime.R;
import io.bigsoft.android.popcorntime.model.Trailer;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private Context mContext;
    private static List<Trailer> mList;

    public TrailersAdapter(Context mContext, List<Trailer> list) {
        this.mContext = mContext;
        mList = list;
    }

    @NonNull
    @Override
    public TrailersAdapter.TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_card,parent,false);

        return new TrailersAdapter.TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapter.TrailersViewHolder holder, int position) {
        holder.trailerTitle.setText(mList.get(position).getName());
        Glide.with(mContext)
                .load(mList.get(position).getYoutubeThumbnailUri())
                .into(holder.trailerThumbnail);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_trailer)
        ImageView trailerThumbnail;
        @BindView(R.id.tv_trailer_title)
        TextView trailerTitle;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mList.get(position).getYoutubePlayerUri())));
                    }
                }
            });
        }
    }

    public void updateTrailers(List<Trailer> trailers) {
        mList.addAll(trailers);
        notifyDataSetChanged();
    }
}
