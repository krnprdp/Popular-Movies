package com.pradeep.nanodegree.popularmovies;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    String id = "", poster = "", title = "", synopsis = "", rating = "", release = "";

    public MovieDetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle args = getArguments();
        id = args.getString("id");
        poster = args.getString("poster");
        title = args.getString("title");
        synopsis = args.getString("synopsis");
        rating = args.getString("rating");
        release = args.getString("release");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        TextView tvSynopsis = (TextView) rootView.findViewById(R.id.tvSynopsis);
        TextView tvRelease = (TextView) rootView.findViewById(R.id.tvRelease);
        TextView tvRating = (TextView) rootView.findViewById(R.id.tvRating);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        tvTitle.setText(title);
        tvRelease.setText("Release Date: " + release);
        tvRating.setText("Rating: " + rating + "/10");
        tvSynopsis.setText("Synopsis:\n" + synopsis);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.ivPoster);

        Picasso.with(getActivity()).load(poster).into(ivPoster);

        new loadTrailersandReviews().execute(id);

        return rootView;
    }

    class loadTrailersandReviews extends AsyncTask<String, Void, String> {

        private ProgressDialog progressdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String title = getResources().getString(R.string.progress_title);
            String message = getResources().getString(R.string.progress_message);

            progressdialog = ProgressDialog.show(getActivity(), title, message);

        }

        @Override
        protected String doInBackground(String... params) {
            String movie_id = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonResponse = null, jsonResponse2 = null;
            StringBuffer stringBuffer;

            String BASE_URL = "http://api.themoviedb.org/3/movie/";
            String videos_endpoint = "videos";
            String reviews_endpoint = "videos";
            String API_PARAM = "api_key";

            try {
                Uri uri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(movie_id)
                        .appendPath(videos_endpoint)
                        .appendQueryParameter(API_PARAM, tmdb.getKey())
                        .build();

                URL url = new URL(uri.toString());

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

                Log.d("MovieDetailsFragment", "Built URL: " + url);
                Log.d("MovieDetailsFragment", "JSONrespone: " + jsonResponse);

                Uri uri2 = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(movie_id)
                        .appendPath(reviews_endpoint)
                        .appendQueryParameter(API_PARAM, tmdb.getKey())
                        .build();

                URL url2 = new URL(uri2.toString());

                urlConnection = (HttpURLConnection) url2.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream2 = urlConnection.getInputStream();

                if (inputStream2 == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream2));
                stringBuffer = new StringBuffer();
                String line2;

                while ((line2 = reader.readLine()) != null) {
                    stringBuffer.append(line2 + "\n");
                }

                if (stringBuffer.length() == 0) {
                    return null;
                }
                jsonResponse2 = stringBuffer.toString();


            } catch (MalformedURLException e) {
                Log.e("MovieDetailsFragment", e.toString());
            } catch (IOException e) {
                Log.e("MovieDetailsFragment", e.toString());
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("MovieDetailsFragment", e.toString());
                    }
                }
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);
            progressdialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject movie = jsonArray.getJSONObject(i);

                    Log.d("MovieDetailsFragment", "Trailer " + i + " " + movie.get("key"));
                }

            } catch (JSONException e) {
                Log.e("MovieDetailsFragment", e.toString());
            }
        }


    }
}
