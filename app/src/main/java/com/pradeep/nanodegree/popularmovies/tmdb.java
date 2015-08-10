package com.pradeep.nanodegree.popularmovies;

/**
 * Created by Pradeep on 8/9/15.
 * <p/>
 * Remember to add your tmdb API Key here. The Key is required for accessing themoviedb.org database from within the app.
 * <p/>
 * THE APP WILL CRASH WITHOUT A VALID API Key!!
 * <p/>
 * Due to copyright reasons, it is illegal to share API KEY publicly.
 * <p/>
 * You can generate you API KEY at https://www.themoviedb.org/documentation/api
 */

public class tmdb {

    private static String api_key = ""; /* Place Your API Key here */

    static String getKey() {
        return api_key;
    }

}
