package com.devandroid.tmsearch.Retrofit;

import android.util.Log;

import com.devandroid.tmsearch.Model.MoviesRequest;
import com.devandroid.tmsearch.Model.ReviewsRequest;
import com.devandroid.tmsearch.Model.VideosRequest;
import com.devandroid.tmsearch.Network.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String LOG_TAG = RetrofitClient.class.getSimpleName();

    private listReceivedListenter mListReceivedListenter;

    public interface listReceivedListenter {
        void listReceived(MoviesRequest moviesRequest);
        void videosReceived(VideosRequest videosRequest);
        void reviewsReceived(ReviewsRequest reviewsRequest);
        void loadFailure();
    }

    public RetrofitClient(listReceivedListenter listener) {
        mListReceivedListenter = listener;
    }

    public void getMostPopularRequest(String page) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getMostPopularRequest(Network.API_KEY, page).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    String elements = moviesRequest!=null ? Integer.toString(moviesRequest.getSize()) : "0";
                    Log.d(LOG_TAG, "Load successful with " + elements + " elements!");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MoviesRequest> call, Throwable t) {
                mListReceivedListenter.loadFailure();
                Log.d(LOG_TAG, "Retrofit load error");
            }
        });
    }

    public void getTopRatedRequest(String page) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getTopRatedRequest(Network.API_KEY, page).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    String elements = moviesRequest!=null ? Integer.toString(moviesRequest.getSize()) : "0";
                    Log.d(LOG_TAG, "Load successful with " + elements + " elements!");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MoviesRequest> call, Throwable t) {
                mListReceivedListenter.loadFailure();
                Log.d(LOG_TAG, "Retrofit load error");
            }
        });
    }

    public void getNowPlayingRequest(String page) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getNowPlayingRequest(Network.API_KEY, page).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    String elements = moviesRequest!=null ? Integer.toString(moviesRequest.getSize()) : "0";
                    Log.d(LOG_TAG, "Load successful with " + elements + " elements!");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MoviesRequest> call, Throwable t) {
                mListReceivedListenter.loadFailure();
                Log.d(LOG_TAG, "Retrofit load error");
            }
        });
    }

    public void getUpcomingRequest(String page) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getUpcomingRequest(Network.API_KEY, page).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    String elements = moviesRequest!=null ? Integer.toString(moviesRequest.getSize()) : "0";
                    Log.d(LOG_TAG, "Load successful with " + elements + " elements!");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MoviesRequest> call, Throwable t) {
                mListReceivedListenter.loadFailure();
                Log.d(LOG_TAG, "Retrofit load error");
            }
        });
    }

    public void getVideosRequest(String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getVideosRequest(id, Network.API_KEY).enqueue(new Callback<VideosRequest>() {
            @Override
            public void onResponse(Call<VideosRequest> call, Response<VideosRequest> response) {
                try {
                    VideosRequest videosRequest = response.body();
                    mListReceivedListenter.videosReceived(videosRequest);
                    String elements = videosRequest!=null ? Integer.toString(videosRequest.getSize()) : "0";
                    Log.d(LOG_TAG, "Load successful with " + elements + " elements!");

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<VideosRequest> call, Throwable t) {
                mListReceivedListenter.loadFailure();
                Log.d(LOG_TAG, "Retrofit load error");
            }
        });
    }

    public void getReviewsRequest(String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getReviewsRequest(id, Network.API_KEY).enqueue(new Callback<ReviewsRequest>() {
            @Override
            public void onResponse(Call<ReviewsRequest> call, Response<ReviewsRequest> response) {
                try {
                    ReviewsRequest reviewsRequest = response.body();
                    mListReceivedListenter.reviewsReceived(reviewsRequest);
                    String elements = reviewsRequest!=null ? Integer.toString(reviewsRequest.getSize()) : "0";
                    Log.d(LOG_TAG, "Load successful with " + elements + " elements!");

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ReviewsRequest> call, Throwable t) {
                mListReceivedListenter.loadFailure();
                Log.d(LOG_TAG, "Retrofit load error");
            }
        });
    }

    public void searchMovieRequest(final String strQuery, String page) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.searchMovieRequest(Network.API_KEY, strQuery, page).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    String elements = moviesRequest!=null ? Integer.toString(moviesRequest.getSize()) : "0";
                    Log.d(LOG_TAG, "Load successful with " + elements + " elements!");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MoviesRequest> call, Throwable t) {
                mListReceivedListenter.loadFailure();
                Log.d(LOG_TAG, "Retrofit load error");
            }
        });
    }
}
