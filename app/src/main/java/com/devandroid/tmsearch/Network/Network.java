package com.devandroid.tmsearch.Network;


public final class Network {

    public static String API_KEY;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOST_POPULAR_SEARCH = "movie/popular?";
    public static final String TOP_RATED_SEARCH = "movie/top_rated?";
    public static final String NOW_PLAYING_SEARCH = "movie/now_playing?";
    public static final String UPCOMING_SEARCH = "movie/upcoming?";
    public static final String VIDEOS_SEARCH = "movie/{id}/videos?";
    public static final String REVIEWS_SEARCH = "movie/{id}/reviews?";

    public static String URL_POSTER_SIZE_185PX(String strPath) { return "https://image.tmdb.org/t/p//w185/"+strPath; }
    public static String URL_POSTER_SIZE_780PX(String strPath) { return "https://image.tmdb.org/t/p//w780/"+strPath; }

    /**
     * Stores the user api key
     * @param strKey
     */
    public static void setApiKey(String strKey) {
        API_KEY = strKey;
    }


}
