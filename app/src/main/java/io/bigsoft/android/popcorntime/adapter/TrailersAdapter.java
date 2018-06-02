package io.bigsoft.android.popcorntime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.bigsoft.android.popcorntime.model.Trailer;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private Context mContext;
    private static List<Trailer> mList;

    public final static String TRAILER_NAME_KEY = "name";
    public final static String TRAILER_ID_KEY = "id";
    public final static String TRAILER_SITE_KEY = "site";
    public final static String TRAILER_KEY_KEY = "key";
    public final static String TRAILER_TYPE_KEY = "type";

    public TrailersAdapter(Context mContext, List<Trailer> list) {
        this.mContext = mContext;
        mList = list;
    }

    @NonNull
    @Override
    public TrailersAdapter.TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapter.TrailersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder {
        public TrailersViewHolder(View itemView) {
            super(itemView);
        }
    }
}
