package com.devandroid.tmsearch.Retrofit;

import android.util.Log;

import com.devandroid.tmsearch.Model.MoviesRequest;
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
        void loadFailure();
    }

    public RetrofitClient(listReceivedListenter listener) {
        mListReceivedListenter = listener;
    }

    public void getMostPopularRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getMostPopularRequest(Network.API_KEY).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    Log.d(LOG_TAG, "Load successful with " + moviesRequest.getSize() + " elements!");
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

    public void getTopRatedRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getTopRatedRequest(Network.API_KEY).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    Log.d(LOG_TAG, "Load successful with " + moviesRequest.getSize() + " elements!");
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

    public void getNowPlayingRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getNowPlayingRequest(Network.API_KEY).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    Log.d(LOG_TAG, "Load successful with " + moviesRequest.getSize() + " elements!");
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

    public void getUpcomingRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getUpcomingRequest(Network.API_KEY).enqueue(new Callback<MoviesRequest>() {
            @Override
            public void onResponse(Call<MoviesRequest> call, Response<MoviesRequest> response) {
                try {
                    MoviesRequest moviesRequest = response.body();
                    mListReceivedListenter.listReceived(moviesRequest);
                    Log.d(LOG_TAG, "Load successful with " + moviesRequest.getSize() + " elements!");
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
