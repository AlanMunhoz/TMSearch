package com.devandroid.tmsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devandroid.tmsearch.Model.Movie;
import com.devandroid.tmsearch.Network.Network;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ListViewHolder> {

    private ListItemClickListener mOnClickListener;
    private ArrayList<Movie> mListItems;
    private Context mContext;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
        void onListEndlessScroll();
    }

    MovieAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    public void setListAdapter(ArrayList<Movie> listItems) {
        mListItems = listItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mListItems==null)
            return 0;
        return mListItems.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivFigure;

        ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ivFigure = itemView.findViewById(R.id.iv_figure);
        }

        void bind(int listIndex) {
            Picasso.with(mContext).load(Network.URL_POSTER_SIZE_185PX(mListItems.get(listIndex).mStrPosterPath)).into(ivFigure);

            /**
             * The last element on the list generate callback to load more
             */
            if(listIndex == mListItems.size()-1) {
                mOnClickListener.onListEndlessScroll();
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}