package com.devandroid.tmsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.devandroid.tmsearch.Model.MoviesRequest;
import com.devandroid.tmsearch.Model.ReviewsRequest;
import com.devandroid.tmsearch.Model.VideosRequest;
import com.devandroid.tmsearch.Network.Network;
import com.devandroid.tmsearch.Retrofit.RetrofitClient;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RetrofitClient.listReceivedListenter,
        ListAdapter.ListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    /**
     * intent/bundle
     */
    public static final String EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE = "extra_main_act_detail_act_movie";

    /**
     * UI components
     */
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRvListMovies;

    /**
     * Data
     */
    private RetrofitClient mRetrofitClient;
    private ListAdapter mAdapter;
    private MoviesRequest mMoviesRequest;
    private ArrayList<ListItem> mLstMovieItems;

    private static final int MOST_POPULAR = 0;
    private static final int TOP_RATED = 1;
    private static final int NOW_PLAYING = 2;
    private static final int UPCOMING = 3;
    private static final int FAVORITES = 4;

    private int mLastSelection = MOST_POPULAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mRvListMovies = findViewById(R.id.rv_list_movies);
        mSwipeRefresh = findViewById(R.id.sr_swipeRefresh);

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);


        mSwipeRefresh.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRvListMovies.setLayoutManager(layoutManager);
        mRvListMovies.setHasFixedSize(true);

        mAdapter = new ListAdapter(MainActivity.this);
        mRvListMovies.setAdapter(mAdapter);


        /**
         * do the first request to populate recycler view
         */
        mActionBar.setTitle(getString(R.string.most_popular_title));
        mLastSelection = MOST_POPULAR;
        mSwipeRefresh.setRefreshing(true);
        mRetrofitClient = new RetrofitClient(this);
        mRetrofitClient.getMostPopularRequest();

        /**
         * Set navigation view select with the first one
         */
        //mNavigationView.setCheckedItem(R.id.nav_most_popular);
        mNavigationView.getMenu().getItem(0).setChecked(true);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.nav_most_popular:
                mActionBar.setTitle(getString(R.string.most_popular_title));
                mLastSelection = MOST_POPULAR;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getMostPopularRequest();
                break;

            case R.id.nav_top_rated:
                mActionBar.setTitle(getString(R.string.top_rated_title));
                mLastSelection = TOP_RATED;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getTopRatedRequest();
                break;

            case R.id.nav_now_playing:
                mActionBar.setTitle(getString(R.string.now_playing_title));
                mLastSelection = NOW_PLAYING;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getNowPlayingRequest();
                break;

            case R.id.nav_upcoming:
                mActionBar.setTitle(getString(R.string.upcoming_title));
                mLastSelection = UPCOMING;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getUpcomingRequest();
                break;

            case R.id.nav_favorites:
                mActionBar.setTitle(getString(R.string.favorites_title));
                mMoviesRequest = null;
                showMovieList();
                break;

            case R.id.nav_exit:
                mActionBar.setTitle(getString(R.string.exit_title));
                super.onBackPressed();
                break;

            default:
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public void listReceived(MoviesRequest moviesRequest) {

        mSwipeRefresh.setRefreshing(false);


        /**
         * Write list with name of recipes
         */
        if(moviesRequest!=null) {
            try {
                mMoviesRequest = moviesRequest;
                showMovieList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void videosReceived(VideosRequest videosRequest) { }

    @Override
    public void reviewsReceived(ReviewsRequest reviewsRequest) { }

    @Override
    public void loadFailure() {

        mSwipeRefresh.setRefreshing(false);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        if(mLastSelection == FAVORITES) {
            //intent.putExtra(EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE, mLstFavoriteEntries.get(clickedItemIndex).getMovie());
        } else {
            intent.putExtra(EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE, Parcels.wrap(mMoviesRequest.getItem(clickedItemIndex)));
        }
        startActivity(intent);

    }

    @Override
    public void onRefresh() {

        mRetrofitClient = new RetrofitClient(this);
        switch (mLastSelection) {
            case MOST_POPULAR:
                mRetrofitClient.getMostPopularRequest();
                break;
            case TOP_RATED:
                mRetrofitClient.getTopRatedRequest();
                break;
            case NOW_PLAYING:
                mRetrofitClient.getNowPlayingRequest();
                break;
            case UPCOMING:
                mRetrofitClient.getUpcomingRequest();
                break;
        }

    }

    private void showMovieList() {

        mLstMovieItems = new ArrayList<>();
        if(mMoviesRequest != null) {
            for (int i = 0; i < mMoviesRequest.getSize(); i++) {
                mLstMovieItems.add(new ListItem(mMoviesRequest.getItem(i).getmStrTitle(), Network.URL_POSTER_SIZE_185PX(mMoviesRequest.getItem(i).mStrPosterPath)));
            }
        }
        mAdapter.setListAdapter(mLstMovieItems);
    }

}
