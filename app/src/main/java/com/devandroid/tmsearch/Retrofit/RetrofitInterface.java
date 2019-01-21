package com.devandroid.tmsearch.Retrofit;

import com.devandroid.tmsearch.Model.MoviesRequest;
import com.devandroid.tmsearch.Model.ReviewsRequest;
import com.devandroid.tmsearch.Model.VideosRequest;
import com.devandroid.tmsearch.Network.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET(Network.MOST_POPULAR_SEARCH)
    Call<MoviesRequest> getMostPopularRequest(@Query("api_key") String apiKey, @Query("page") String page);

    @GET(Network.TOP_RATED_SEARCH)
    Call<MoviesRequest> getTopRatedRequest(@Query("api_key") String apiKey, @Query("page") String page);

    @GET(Network.NOW_PLAYING_SEARCH)
    Call<MoviesRequest> getNowPlayingRequest(@Query("api_key") String apiKey, @Query("page") String page);

    @GET(Network.UPCOMING_SEARCH)
    Call<MoviesRequest> getUpcomingRequest(@Query("api_key") String apiKey, @Query("page") String page);

    @GET(Network.SEARCH_MOVIE)
    Call<MoviesRequest> searchMovieRequest(@Query("api_key") String apiKey, @Query("query") String query);

    @GET(Network.VIDEOS_SEARCH)
    Call<VideosRequest> getVideosRequest(@Path("id") String id, @Query("api_key") String apiKey);

    @GET(Network.REVIEWS_SEARCH)
    Call<ReviewsRequest> getReviewsRequest(@Path("id") String id, @Query("api_key") String apiKey);

}