package com.pradeep.nanodegree.popularmovies;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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

    TextView tvTitle, tvSynopsis, tvRelease, tvRating, tvReviews;
    ImageButton favorite;
    LinearLayout trailer1, trailer2;
    boolean fav_set = false;
    String t1 = null, t2 = null;

    Uri uri = Uri.parse("content://com.pradeep.nanodegree.popularmovies.provider");
    ContentResolver resolver;

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

        resolver = getActivity().getContentResolver();
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvSynopsis = (TextView) rootView.findViewById(R.id.tvSynopsis);
        tvRelease = (TextView) rootView.findViewById(R.id.tvRelease);
        tvRating = (TextView) rootView.findViewById(R.id.tvRating);
        tvReviews = (TextView) rootView.findViewById(R.id.tvReviews);
        favorite = (ImageButton) rootView.findViewById(R.id.favorite);


        trailer1 = (LinearLayout) rootView.findViewById(R.id.trailer1);
        trailer2 = (LinearLayout) rootView.findViewById(R.id.trailer2);

        Cursor resultCursor = resolver.query(uri, null,
                id, null, null);


        if (resultCursor != null) {
            favorite.setBackgroundResource(R.mipmap.ic_star_black_24dp);
            fav_set = true;
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!fav_set) {
                    ContentValues cv = new ContentValues();
                    cv.put("id", id);
                    cv.put("title", title);
                    cv.put("poster", poster);
                    cv.put("synopsis", synopsis);
                    cv.put("rating", rating);
                    cv.put("release", release);
                    favorite.setBackgroundResource(R.mipmap.ic_star_black_24dp);
                    resolver.insert(uri, cv);
                    fav_set = true;
                } else {
                    favorite.setBackgroundResource(R.mipmap.ic_star_border_black_24dp);
                    resolver.delete(uri, id, null);
                    fav_set = false;
                }

            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        tvTitle.setText(title);
        tvRelease.setText("Release Date: " + release);
        tvRating.setText("Rating: " + rating + "/10");
        tvSynopsis.setText("\n" + synopsis);
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
            StringBuffer stringBuffer;
            String jsonResponse = null;

            String BASE_URL = "http://api.themoviedb.org/3/movie/";
            String API_PARAM = "api_key";

            try {
                Uri uri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(movie_id)
                        .appendQueryParameter(API_PARAM, tmdb.getKey())
                        .appendQueryParameter("append_to_response", "reviews")
                        .build();

                URL url = new URL(uri.toString() + ",trailers");

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
                JSONArray reviews = (JSONArray) ((JSONObject) jsonObject.get("reviews")).get("results");

                String appended_review = "\n";

                for (int i = 0; i < reviews.length(); i++) {
                    appended_review += (i + 1) + ". " + reviews.getJSONObject(i).get("content").toString() + "\n-- " + reviews.getJSONObject(i).get("author").toString();
                    if (i != reviews.length() - 1)
                        appended_review += "\n\n";
                }

                tvReviews.setText(appended_review);


                JSONArray trailers = (JSONArray) ((JSONObject) jsonObject.get("trailers")).get("youtube");


                if (trailers.length() >= 2) {
                    t1 = trailers.getJSONObject(0).getString("source");
                    t2 = trailers.getJSONObject(1).getString("source");
                } else if (trailers.length() == 1) {
                    t1 = trailers.getJSONObject(0).getString("source");
                }

                trailer1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (t1 == null)
                            Toast.makeText(getActivity(), "No Trailers For this movie", Toast.LENGTH_SHORT).show();
                        else {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + t1));
                                getActivity().startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + t1));
                                getActivity().startActivity(intent);
                            }
                        }
                    }
                });

                trailer2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (t2 == null)
                            Toast.makeText(getActivity(), "No more Trailers For this movie", Toast.LENGTH_SHORT).show();
                        else {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + t2));
                                getActivity().startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + t2));
                                getActivity().startActivity(intent);
                            }
                        }
                    }
                });

            } catch (JSONException e) {
                Log.e("MovieDetailsFragment", e.toString());
            }
        }


    }
}
