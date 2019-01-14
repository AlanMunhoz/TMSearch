package com.devandroid.tmsearch.Network;


public final class Network {

    public static String API_KEY;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOST_POPULAR_SEARCH = "movie/popular?";
    public static final String TOP_RATED_SEARCH = "movie/top_rated?";
    public static final String NOW_PLAYING_SEARCH = "movie/now_playing?";
    public static final String UPCOMING_SEARCH = "movie/upcoming?";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/";
    public static final String IMAGE_POSTER_SIZE_185PX = "/w185/";
    public static final String IMAGE_POSTER_SIZE_780PX = "/w780/";

    public static String VIDEOS_URL(String id) { return "movie/"+id+"/videos?api_key="; }
    public static String REVIEWS_URL(String id) { return "movie/"+id+"/reviews?api_key="; }

    /**
     * Stores the user api key
     * @param strKey
     */
    public static void setApiKey(String strKey) {
        API_KEY = strKey;
    }


}
