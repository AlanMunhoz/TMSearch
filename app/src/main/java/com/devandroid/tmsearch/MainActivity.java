package com.devandroid.tmsearch;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.devandroid.tmsearch.Firebase.FirebaseManager;
import com.devandroid.tmsearch.Firebase.FirebaseCallback;
import com.devandroid.tmsearch.Model.Movie;
import com.devandroid.tmsearch.Model.MoviesRequest;
import com.devandroid.tmsearch.Model.ReviewsRequest;
import com.devandroid.tmsearch.Model.VideosRequest;
import com.devandroid.tmsearch.Network.Network;
import com.devandroid.tmsearch.Preferences.Preferences;
import com.devandroid.tmsearch.Retrofit.RetrofitClient;
import com.devandroid.tmsearch.RoomDatabase.MainViewModel;
import com.devandroid.tmsearch.Util.Utils;
import com.devandroid.tmsearch.widget.WidgetService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RetrofitClient.listReceivedListenter,
        MovieAdapter.ListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        FirebaseCallback
{

    /**
     * intent/bundle
     */
    public static final String EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE = "extra_main_act_detail_act_movie";
    private static final String INTRA_MAIN_ACT_LAST_SELECTION = "intra_main_act_last_selection";
    private static final String INTRA_MAIN_ACT_CURRENT_SELECTION = "intra_main_act_current_selection";
    private static final String INTRA_MAIN_ACT_MOVIE_REQUEST = "intra_main_act_movie_request";
    private static final String INTRA_MAIN_ACT_SEARCH_QUERY = "intra_main_act_search_query";

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
    private static final int SEARCH_MOVIE = 5;

    /**
     * UI components
     */
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRvListMovies;
    private TextView mTvNavHeaderName;
    private TextView mTvNavHeaderEmail;
    private ImageView mIvNavHeaderImage;

    /**
     * Data
     */
    private RetrofitClient mRetrofitClient;
    private MovieAdapter mAdapter;
    private MoviesRequest mMoviesRequest;
    private String mLastSearchQuery;
    private int mLastSelection = MOST_POPULAR;
    private int mCurrentSelection = MOST_POPULAR;
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
        mTvNavHeaderName = mNavigationView.getHeaderView(0).findViewById(R.id.tvNavHeaderName);
        mTvNavHeaderEmail = mNavigationView.getHeaderView(0).findViewById(R.id.tvNavHeaderEmail);
        mIvNavHeaderImage = mNavigationView.getHeaderView(0).findViewById(R.id.ivNavHeaderImage);

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
         * subscribe firebase auth listener
         */
        FirebaseManager.addListener(MainActivity.this);

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
         * Restore the search if exists
         */
        if (savedInstanceState != null) {

            mLastSelection = savedInstanceState.getInt(INTRA_MAIN_ACT_LAST_SELECTION);
            mCurrentSelection = savedInstanceState.getInt(INTRA_MAIN_ACT_CURRENT_SELECTION);

            if(mCurrentSelection==SEARCH_MOVIE) {
                mCurrentSelection = mLastSelection;
                onUpdateRequest();
            }

            mLastSearchQuery = savedInstanceState.getString(INTRA_MAIN_ACT_SEARCH_QUERY);
            mMoviesRequest = Parcels.unwrap(savedInstanceState.getParcelable(INTRA_MAIN_ACT_MOVIE_REQUEST));
        }

        /**
         * Set Title actionbar and adapter with list movies
         */
        showMovieList();

        /**
         * Set navigation view select with the first one
         * Set User Name and Email on header_layout of Navigation View
         */
        //mNavigationView.setCheckedItem(R.id.nav_most_popular);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        mTvNavHeaderName.setText(FirebaseManager.FirebaseAuthGetUserName());
        mTvNavHeaderEmail.setText(FirebaseManager.FirebaseAuthGetUserEmail());

    }

    @Override
    protected void onPause() {
        super.onPause();

        Utils.AlertDialogDismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /**
         * unsubscribe firebase auth listener
         */
        FirebaseManager.removeListener(MainActivity.this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /**
         * Store the last search did by user
         */
        outState.putInt(INTRA_MAIN_ACT_CURRENT_SELECTION, mCurrentSelection);
        outState.putInt(INTRA_MAIN_ACT_LAST_SELECTION, mLastSelection);
        outState.putString(INTRA_MAIN_ACT_SEARCH_QUERY, mLastSearchQuery);
        outState.putParcelable(INTRA_MAIN_ACT_MOVIE_REQUEST, Parcels.wrap(mMoviesRequest));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        /**
         * capturing reference of searchView
         */
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        /**
         * customizing searchView
         */
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getColor(R.color.clLightTextColor));
        searchEditText.setHintTextColor(getColor(R.color.clLightTextColor));
        searchEditText.setHint("Search");
        searchEditText.setHintTextColor(getColor(R.color.colorSearchViewHint));

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                restoreLastSelection();
                onUpdateRequest();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if(!query.isEmpty()) {
                    mLastSearchQuery = query;
                    changeSelection(SEARCH_MOVIE);
                    onUpdateRequest();
                }
                return false;
            }
        });

        /*
        if(mCurrentSelection == SEARCH_MOVIE) {
            searchItem.expandActionView();
            searchEditText.setText(mLastSearchQuery);
        }
        */

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.nav_most_popular:
                changeSelection(MOST_POPULAR);
                onUpdateRequest();
                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.MOST_POPULAR_MENU);
                break;

            case R.id.nav_top_rated:
                changeSelection(TOP_RATED);
                onUpdateRequest();
                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.TOP_RATED_MENU);
                break;

            case R.id.nav_now_playing:
                changeSelection(NOW_PLAYING);
                onUpdateRequest();
                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.NOW_LAYING_MENU);
                break;

            case R.id.nav_upcoming:
                changeSelection(UPCOMING);
                onUpdateRequest();
                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.UPCOMING_MENU);
                break;

            case R.id.nav_favorites:
                changeSelection(FAVORITES);
                onUpdateRequest();
                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.FAVORITES_MENU);
                break;

            case R.id.nav_config:
                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.CONFIG_MENU);
                startActivity(new Intent(MainActivity.this, ConfigActivity.class));
                break;

            case R.id.nav_exit:

                Utils.AlertDialogStart(MainActivity.this,
                        getString(R.string.exit_confirmation_title),
                        getString(R.string.exit_confirmation_message),
                        getString(R.string.exit_confirmation_pos),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                FirebaseManager.FirebaseAuthStartSignOut();
                                FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.EXIT_MENU);
                                Utils.AlertDialogDismiss();
                                finish();
                            }
                        },
                        getString(R.string.exit_confirmation_neg),
                        null
                );

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
         * Write list
         */
        mMoviesRequest = moviesRequest;
        showMovieList();


        /**
         * Starts service
         */
        if(mCurrentSelection == MOST_POPULAR) {
            startWidgetService();
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

        FirebaseManager.FirebaseAnalyticsLogEvent(FirebaseManager.EventKeys.MOVIE_CLICK_LIST);

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        if(mCurrentSelection == FAVORITES) {
            intent.putExtra(EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE, Parcels.wrap(mLstFavoriteMovies.get(clickedItemIndex)));
        } else {
            intent.putExtra(EXTRA_MAIN_ACT_DETAIL_ACT_MOVIE, Parcels.wrap(mMoviesRequest.getItem(clickedItemIndex)));
        }
        startActivity(intent);

    }

    @Override
    public void onRefresh() {

        onUpdateRequest();
    }

    @Override
    public void mListenerSignInSuccessful() { }

    @Override
    public void mListenerSignInFail(String reason) { }

    @Override
    public void mListenerRegisterSuccessful() { }

    @Override
    public void mListenerRegisterFail(String reason) { }

    @Override
    public void mListenerChangeCredentialsSuccessful() {}

    @Override
    public void mListenerChangeCredentialsFail(String reason) {}

    @Override
    public void mListenerDatabaseSetApiKeySuccessful() {}

    @Override
    public void mListenerDatabaseSetApiKeyFail(String reason) {}

    @Override
    public void mListenerDatabaseGetApiKey(String key) {

        /**
         * restore the api key
         */
        Network.setApiKey(key);

        /**
         * request movies
         */
        onUpdateRequest();

    }

    private void onUpdateRequest() {

        /**
         * Favorites doesn't need to do any request
         */
        if(mCurrentSelection!=FAVORITES) {
            if (Network.API_KEY.equals("")) {
                Utils.AlertDialogStart(this,
                        getString(R.string.alert_api_key_title),
                        getString(R.string.alert_api_key_message),
                        getString(R.string.alert_api_key_btn_pos),
                        null,
                        "",
                        null);
            }
        }

        switch (mCurrentSelection) {
            case MOST_POPULAR:
                mActionBar.setTitle(getString(R.string.most_popular_title));
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient.getMostPopularRequest();
                break;
            case TOP_RATED:
                mActionBar.setTitle(getString(R.string.top_rated_title));
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient.getTopRatedRequest();
                break;
            case NOW_PLAYING:
                mActionBar.setTitle(getString(R.string.now_playing_title));
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient.getNowPlayingRequest();
                break;
            case UPCOMING:
                mActionBar.setTitle(getString(R.string.upcoming_title));
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient.getUpcomingRequest();
                break;
            case FAVORITES:
                mActionBar.setTitle(getString(R.string.favorites_title));
                showMovieList();
                mSwipeRefresh.setRefreshing(false);
                break;
            case SEARCH_MOVIE:
                mActionBar.setTitle(getString(R.string.search_title) + ": \"" +mLastSearchQuery + "\"");
                mSwipeRefresh.setRefreshing(true);
                mRetrofitClient = new RetrofitClient(MainActivity.this);
                mRetrofitClient.searchMovieRequest(mLastSearchQuery);
                break;
        }
    }

    private void changeSelection(int selection) {

        if(selection!=mCurrentSelection) {
            mLastSelection = mCurrentSelection;
            mCurrentSelection = selection;
        }
    }

    private void restoreLastSelection() {

        mCurrentSelection = mLastSelection;
    }

    private void showMovieList() {

        ArrayList<Movie> lstMovie = new ArrayList<>();
        switch (mCurrentSelection) {
            case MOST_POPULAR:
                mActionBar.setTitle(getString(R.string.most_popular_title));
                lstMovie = mMoviesRequest!=null ? mMoviesRequest.getmMovies() : new ArrayList<Movie>();
                break;
            case TOP_RATED:
                mActionBar.setTitle(getString(R.string.top_rated_title));
                lstMovie = mMoviesRequest!=null ? mMoviesRequest.getmMovies() : new ArrayList<Movie>();
                break;
            case NOW_PLAYING:
                mActionBar.setTitle(getString(R.string.now_playing_title));
                lstMovie = mMoviesRequest!=null ? mMoviesRequest.getmMovies() : new ArrayList<Movie>();
                break;
            case UPCOMING:
                mActionBar.setTitle(getString(R.string.upcoming_title));
                lstMovie = mMoviesRequest!=null ? mMoviesRequest.getmMovies() : new ArrayList<Movie>();
                break;
            case FAVORITES:
                mActionBar.setTitle(getString(R.string.favorites_title));
                lstMovie = mLstFavoriteMovies!=null ? mLstFavoriteMovies : new ArrayList<Movie>();
                break;
            case SEARCH_MOVIE:
                mActionBar.setTitle(getString(R.string.search_title) + ": \"" +mLastSearchQuery + "\"");
                lstMovie = mMoviesRequest!=null ? mMoviesRequest.getmMovies() : new ArrayList<Movie>();
                break;
        }
        mAdapter.setListAdapter(lstMovie);

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

                Log.d(MainActivity.LOG_TAG, "onChanged DB");
                mLstFavoriteMovies = new ArrayList<>();
                mLstFavoriteMovies.addAll(favoriteMovies);
                if(mCurrentSelection == FAVORITES) {
                    showMovieList();
                }
            }
        });
    }

    private void startWidgetService()
    {

        if(mMoviesRequest==null) return;

        Preferences.saveStringMovie(this, getString(R.string.appwidget_text));
        ArrayList<String> lstMovies = new ArrayList<>();
        for(int i=0; i<10; i++) {
            Movie movie = mMoviesRequest.getmMovies().get(i);
            String strLine = "Score: " + movie.mStrPopularity + " " + "Title: " + movie.getmStrTitle();
            lstMovies.add(strLine);
        }
        Preferences.saveStringList(this, lstMovies);

        WidgetService.startUpdateWidget(this);
    }
}
