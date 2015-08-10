package com.pradeep.nanodegree.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pradeep on 8/9/15.
 */
public class PopularMoviesFragment extends Fragment {

    String api_key;
    String sort_order;
    String LOG_TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.api_key = args.getString("api_key");
        } else
            this.api_key = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popularmovies, container, false);

        if (api_key != null) {

        } else {
            Toast.makeText(getActivity(), "API Key is Missing.\n" +
                    "Please add a valid key in tmdb.java and recompile", Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }

    class fetchMovies extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonResponse = null;
            StringBuffer stringBuffer = null;

            String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            String SORT_PARAM = "sort_by";
            String API_PARAM = "api_key";
            String sortParam = params[0] + ".desc";

            try {

                Uri uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sortParam)
                        .appendQueryParameter(API_PARAM, api_key)
                        .build();

                URL url = new URL(uri.toString());

                Log.d(LOG_TAG, "Built URL: " + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0) {
                    return null;
                }

                jsonResponse = stringBuffer.toString();

                Log.d(LOG_TAG, "JSON RESPONSE:\n" + jsonResponse);


            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, e.toString());
                    }
                }
            }

            return jsonResponse;
        }
    }


}
