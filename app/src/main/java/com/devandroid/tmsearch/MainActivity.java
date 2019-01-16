package com.devandroid.tmsearch;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;


import com.devandroid.tmsearch.Model.Movie;
import com.devandroid.tmsearch.Model.MoviesRequest;
import com.devandroid.tmsearch.Model.ReviewsRequest;
import com.devandroid.tmsearch.Model.VideosRequest;
import com.devandroid.tmsearch.Retrofit.RetrofitClient;
import com.devandroid.tmsearch.RoomDatabase.MainViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RetrofitClient.listReceivedListenter,
        MovieAdapter.ListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    /**
     * intent/bundle
     */
    public static final String EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE = "extra_main_act_detail_act_movie";
    private static final String INTRA_MAIN_ACT_LAST_SELECTION = "intra_main_act_last_selection";
    private static final String INTRA_MAIN_ACT_MOVIE_REQUEST = "intra_main_act_movie_request";

    /**
     * Constants
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MINIMUM_CEL_WIDTH = 250;
    private static final int MOST_POPULAR = 0;
    private static final int TOP_RATED = 1;
    private static final int NOW_PLAYING = 2;
    private static final int UPCOMING = 3;
    private static final int FAVORITES = 4;

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
    private MovieAdapter mAdapter;
    private MoviesRequest mMoviesRequest;
    private int mLastSelection = MOST_POPULAR;
    private ArrayList<Movie> mLstFavoriteMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Getting UI references
         */
        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mRvListMovies = findViewById(R.id.rv_list_movies);
        mSwipeRefresh = findViewById(R.id.sr_swipeRefresh);

        /**
         * Set Toolbar and get actionbar
         */
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        /**
         * Set Drawer Layout
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        /**
         * Set listener to Navigation View
         */
        mNavigationView.setNavigationItemSelectedListener(this);

        /**
         * Set listener to Swipe Refresh
         */
        mSwipeRefresh.setOnRefreshListener(this);

        /**
         * Create retrofit and set listener to
         */
        mRetrofitClient = new RetrofitClient(this);

        /**
         * Setup recycler view, layout manager
         */
        LinearLayoutManager layoutManager = new GridLayoutManager(this, getNCardColumns(this));
        mRvListMovies.setLayoutManager(layoutManager);
        mRvListMovies.setHasFixedSize(true);

        /**
         * Setup movie list adapter
         */
        mAdapter = new MovieAdapter(MainActivity.this);
        mRvListMovies.setAdapter(mAdapter);

        /**
         * Set listener to LiveData
         */
        addLiveDataObserver();

        /**
         * Restore the search if exists and call request movies
         */
        if (savedInstanceState != null) {
            mLastSelection = savedInstanceState.getInt(INTRA_MAIN_ACT_LAST_SELECTION);
            mMoviesRequest = Parcels.unwrap(savedInstanceState.getParcelable(INTRA_MAIN_ACT_MOVIE_REQUEST));
        }

        /**
         * if there is movieRequest show in list, otherwise request by first time
         */
        if(mLastSelection == FAVORITES) {
            //showFavoriteList();
        } else {
            if(mMoviesRequest != null) {
                showMovieList();
            } else {
                mLastSelection = MOST_POPULAR;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient.getMostPopularRequest();
            }
        }
        setTitleActionBar();

        /**
         * Set navigation view select with the first one
         */
        //mNavigationView.setCheckedItem(R.id.nav_most_popular);
        mNavigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /**
         * Store the last search did by user
         */
        outState.putInt(INTRA_MAIN_ACT_LAST_SELECTION, mLastSelection);
        outState.putParcelable(INTRA_MAIN_ACT_MOVIE_REQUEST, Parcels.wrap(mMoviesRequest));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.nav_most_popular:
                mLastSelection = MOST_POPULAR;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getMostPopularRequest();
                break;

            case R.id.nav_top_rated:
                mLastSelection = TOP_RATED;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getTopRatedRequest();
                break;

            case R.id.nav_now_playing:
                mLastSelection = NOW_PLAYING;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getNowPlayingRequest();
                break;

            case R.id.nav_upcoming:
                mLastSelection = UPCOMING;
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(this);
                mRetrofitClient.getUpcomingRequest();
                break;

            case R.id.nav_favorites:
                mLastSelection = FAVORITES;
                showMovieList();
                break;

            case R.id.nav_exit:
                super.onBackPressed();
                break;

            default:
                break;
        }

        setTitleActionBar();
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
         * Write list
         */
        if(moviesRequest!=null) {
            mMoviesRequest = moviesRequest;
            showMovieList();
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
            intent.putExtra(EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE, Parcels.wrap(mLstFavoriteMovies.get(clickedItemIndex)));
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
            case FAVORITES:
                mSwipeRefresh.setRefreshing(false);
                break;
        }
    }

    private void setTitleActionBar() {

        if(mActionBar != null) {

            switch (mLastSelection) {

                case MOST_POPULAR:
                    mActionBar.setTitle(getString(R.string.most_popular_title));
                    break;

                case TOP_RATED:
                    mActionBar.setTitle(getString(R.string.top_rated_title));
                    break;

                case NOW_PLAYING:
                    mActionBar.setTitle(getString(R.string.now_playing_title));
                    break;

                case UPCOMING:
                    mActionBar.setTitle(getString(R.string.upcoming_title));
                    break;

                case FAVORITES:
                    mActionBar.setTitle(getString(R.string.favorites_title));
                    break;

                default:
                    mActionBar.setTitle(getString(R.string.most_popular_title));
                    break;
            }
        }
    }

    private void showMovieList() {

        if(mLastSelection == FAVORITES) {
            mAdapter.setListAdapter(mLstFavoriteMovies);
        } else {
            if (mMoviesRequest != null) {
                mAdapter.setListAdapter(mMoviesRequest.getmMovies());
            } else {
                mAdapter.setListAdapter(new ArrayList<Movie>());
            }
        }
    }

    /**
     * calculate and define how many columns will be present in recycler view
     */
    private int getNCardColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int nColumns = (displayMetrics.widthPixels / MINIMUM_CEL_WIDTH);
        return nColumns;
    }

    /**
     * Adding observer to database, this way allow us to know when database changes occurs.
     */
    private void addLiveDataObserver() {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        /**
         * onChanged runs on the main thread by default
         */
        viewModel.getFavoriteEntries().observe(this, new Observer<List<Movie>>() {

            //onChanged runs on the main thread by default
            @Override
            public void onChanged(@Nullable List<Movie> favoriteMovies) {

                Log.d(MainViewModel.LOG_TAG, "onChanged DB");
                mLstFavoriteMovies = new ArrayList<>();
                for(int i=0;i<favoriteMovies.size();i++) {
                    mLstFavoriteMovies.add(favoriteMovies.get(i));
                }
                if(mLastSelection == FAVORITES) {
                    showMovieList();
                }
            }
        });
    }
}
