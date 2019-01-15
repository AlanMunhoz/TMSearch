package com.devandroid.tmsearch;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
    private String mSearchUrl;
    private Menu mFavoriteMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvVideos.setLayoutManager(layoutManager);
        mRvVideos.setHasFixedSize(true);

        mAdapter = new VideoAdapter(DetailActivity.this);
        mRvVideos.setAdapter(mAdapter);

        /**
         * Gets the object passed by intent
         */
        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE)) {
            Bundle data = intent.getExtras();
            Movie movie = null;
            if(data != null) {
                movie = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE));
            }
            if(movie != null) {

                mMovie = movie;

                tvTitle.setText(movie.getmStrTitle());
                Picasso.with(this).load(Network.URL_POSTER_SIZE_780PX(movie.getmStrPosterPath())).into(ivPosterPath);
                tvOverview.setText(movie.getmStrOverview());
                tvVoteAverage.setText(movie.getmStrVoteAverage());
                tvReleaseDate.setText(movie.getmStrReleaseDate());
                tvVoteCount.setText(movie.getmStrVoteCount());
                tvPopularity.setText(movie.getmStrPopularity());

                /**
                 * do the first request to populate recycler view
                 */
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getVideosRequest(mMovie.mStrId);
                mRetrofitClient.getReviewsRequest(mMovie.mStrId);

            }
        }

        setBackgroundImage();

        final Toolbar toolbar = findViewById(R.id.MyToolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapse_toolbar);
        collapsingToolbarLayout.setTitle(mMovie.getmStrTitle());

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

        String videoUrl = mLstVideos.get(clickedItemIndex).getYoutubeVideoUrl();
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

    public void setBackgroundImage() {

        Picasso.with(this).load(Network.URL_POSTER_SIZE_780PX(mMovie.getmStrBackdropPath())).into(mIvBackgroundPath);
    }

    private void showVideosList() {

        try {
            if(mLstVideos!=null) {
                mRvVideos.setVisibility(RecyclerView.VISIBLE);
                mAdapter.setListAdapter(mLstVideos);
            } else {
                mRvVideos.setVisibility(RecyclerView.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReviewsList() {

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
