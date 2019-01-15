package com.devandroid.tmsearch;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.devandroid.tmsearch.Model.Movie;
import com.devandroid.tmsearch.Model.MoviesRequest;
import com.devandroid.tmsearch.Model.ReviewsRequest;
import com.devandroid.tmsearch.Model.Video;
import com.devandroid.tmsearch.Model.VideosRequest;
import com.devandroid.tmsearch.Network.Network;
import com.devandroid.tmsearch.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity
        implements VideoAdapter.ListItemClickListener,
        RetrofitClient.listReceivedListenter{

    /**
     * Constants
     */
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    /**
     * UI components
     */
    private ImageView mIvBackgroundPath;
    private ImageView ivPosterPath;
    private TextView tvTitle;
    private TextView tvVoteAverage;
    private TextView tvReleaseDate;
    private TextView tvOverview;
    private TextView tvVoteCount;
    private TextView tvPopularity;
    private TextView tvReviews;
    private RecyclerView mRvVideos;

    /**
     * Data
     */
    private RetrofitClient mRetrofitClient;
    private VideoAdapter mAdapter;
    private Movie mMovie;
    private ArrayList<Video> mLstVideos;
    private ReviewsRequest mReviewRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /**
         * Getting UI references
         */
        mIvBackgroundPath = findViewById(R.id.ivImageMovie);
        ivPosterPath = findViewById(R.id.ivPosterPath);
        tvTitle = findViewById(R.id.tvTitle);
        tvVoteAverage = findViewById(R.id.tvVoteAverage);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvOverview = findViewById(R.id.tvOverview);
        tvVoteCount = findViewById(R.id.tvVoteCount);
        tvPopularity = findViewById(R.id.tvPopularity);
        tvReviews = findViewById(R.id.tvReviews);
        mRvVideos = findViewById(R.id.rvVideos);

        /**
         * Create retrofit and set listener to
         */
        mRetrofitClient = new RetrofitClient(this);

        /**
         * Setup recycler view, layout manager
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvVideos.setLayoutManager(layoutManager);
        mRvVideos.setHasFixedSize(true);

        /**
         * Setup video list adapter
         */
        mAdapter = new VideoAdapter(DetailActivity.this);
        mRvVideos.setAdapter(mAdapter);

        /**
         * Gets the object passed by intent
         */
        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE)) {
            Bundle data = intent.getExtras();
            if(data != null) {
                mMovie = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE));
            }
            if(mMovie != null) {

                tvTitle.setText(mMovie.getmStrTitle());
                Picasso.with(this).load(Network.URL_POSTER_SIZE_780PX(mMovie.getmStrBackdropPath())).into(mIvBackgroundPath);
                Picasso.with(this).load(Network.URL_POSTER_SIZE_780PX(mMovie.getmStrPosterPath())).into(ivPosterPath);
                tvOverview.setText(mMovie.getmStrOverview());
                tvVoteAverage.setText(mMovie.getmStrVoteAverage());
                tvReleaseDate.setText(mMovie.getmStrReleaseDate());
                tvVoteCount.setText(mMovie.getmStrVoteCount());
                tvPopularity.setText(mMovie.getmStrPopularity());

                /**
                 * do the first request to populate video list
                 */
                mRetrofitClient.getVideosRequest(mMovie.mStrId);
                mRetrofitClient.getReviewsRequest(mMovie.mStrId);

            }
        }

        /**
         * Setup Toolbar and Collapsing toolbar
         */
        final Toolbar toolbar = findViewById(R.id.Toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapse_toolbar);
        collapsingToolbarLayout.setTitle(mMovie.getmStrTitle());

        /**
         * Configuring colors of toolbar
         */
        collapsingToolbarLayout.setExpandedTitleColor(getColor(R.color.clLightTextColor));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.clLightTextColor));
        collapsingToolbarLayout.setContentScrimColor(getColor(R.color.clSelectedBackground));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        String videoUrl = Network.YOUTUBE_VIDEO_URL(mLstVideos.get(clickedItemIndex).getKey());
        Intent target = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        Intent chooser = Intent.createChooser(target, "Open With");
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    @Override
    public void listReceived(MoviesRequest moviesRequest) { }

    @Override
    public void videosReceived(VideosRequest videosRequest) {

        if(videosRequest!=null) {
            mLstVideos = videosRequest.getList();
            showVideosList();
        }
    }

    @Override
    public void reviewsReceived(ReviewsRequest reviewsRequest) {
        if(reviewsRequest!=null) {
            mReviewRequest = reviewsRequest;
            showReviewsList();
        }
    }

    @Override
    public void loadFailure() {

    }

    private void showVideosList() {

        if(mLstVideos!=null) {
            mRvVideos.setVisibility(RecyclerView.VISIBLE);
            mAdapter.setListAdapter(mLstVideos);
        } else {
            mRvVideos.setVisibility(RecyclerView.GONE);
        }
    }

    private void showReviewsList() {

        String strText = "";
        for(int i=0; i<mReviewRequest.getSize(); i++) {
            strText += "[" + mReviewRequest.getItem(i).getmAuthor() + "]" + "\n";
            strText += mReviewRequest.getItem(i).getmContent() + "\n";
            strText += mReviewRequest.getItem(i).getmUrl() + "\n";
            strText += "\n\n";
        }
        if(mReviewRequest.getSize()==0){
            tvReviews.setText(getString(R.string.no_reviews));
        } else {
            tvReviews.setText(strText);
        }
    }

}
