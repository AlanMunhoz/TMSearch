package com.devandroid.tmsearch.Network;

public final class Network {

    /**
     * The Movie Db Api Key
     */
    public static String API_KEY = "";

    /**
     * The Movie Db Retrofit Request Urls
     */
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOST_POPULAR_SEARCH = "movie/popular?";
    public static final String TOP_RATED_SEARCH = "movie/top_rated?";
    public static final String NOW_PLAYING_SEARCH = "movie/now_playing?";
    public static final String UPCOMING_SEARCH = "movie/upcoming?";
    public static final String SEARCH_MOVIE = "search/movie?";
    public static final String VIDEOS_SEARCH = "movie/{id}/videos?";
    public static final String REVIEWS_SEARCH = "movie/{id}/reviews?";

    /**
     * The Movie Db Poster Urls
     */
    public static String URL_POSTER_SIZE_185PX(String strPath) { return "https://image.tmdb.org/t/p//w185/"+strPath; }
    public static String URL_POSTER_SIZE_780PX(String strPath) { return "https://image.tmdb.org/t/p//w780/"+strPath; }

    /**
     * Youtube Url to get image and vídeo of a id video
     */
    public static final String YOUTUBE_IMAGE_URL(String id) { return "http://i3.ytimg.com/vi/"+id+"/hqdefault.jpg"; }
    public static final String YOUTUBE_VIDEO_URL(String id) { return "https://www.youtube.com/watch?v="+id; }

    /**
     * Stores the user tmdb api key
     * @param strKey
     */
    public static void setApiKey(String strKey) {
        API_KEY = strKey;
    }

}
