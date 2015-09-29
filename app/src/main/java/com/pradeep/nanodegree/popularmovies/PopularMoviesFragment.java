package com.pradeep.nanodegree.popularmovies;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Pradeep on 8/9/15.
 * <p/>
 * This fragment makes the API call and loads the Top Movies using a custom Adapter.
 */
public class PopularMoviesFragment extends Fragment {

    String api_key;
    String sort_order;
    private String[] titles = null;
    private String[] posters = null;
    private ArrayList<Movie> movies;
    GridView gridView;
    TextView tv;
    Button btn;
    ContentResolver resolver;
    Uri uri = Uri.parse("content://com.pradeep.nanodegree.popularmovies.provider");
    String LOG_TAG = getClass().getSimpleName();
    static Boolean flag;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            movies = new ArrayList<Movie>();
            flag = false;

        } else {
            Log.d(LOG_TAG, "From savedInstanceState - Orientation Change? ");
            movies = savedInstanceState.getParcelableArrayList("movies");

            flag = true;

        }

        if (args != null) {
            this.api_key = args.getString("api_key");
            this.sort_order = args.getString("sort_order");
            if (sort_order == null) {
                sort_order = "POPULARITY";
            }


        } else {
            this.api_key = tmdb.getKey(); // In case of tablet mode
            sort_order = "POPULARITY";
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popularmovies, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        tv = (TextView) rootView.findViewById(R.id.tvError);
        btn = (Button) rootView.findViewById(R.id.btnRefresh);

        resolver = getActivity().getContentResolver();

        if (api_key != null) {
            if (sort_order == "favourites") {

                Cursor resultCursor = resolver.query(uri, null,
                        "@", null, null);


                if (resultCursor != null) {
                    resultCursor.moveToFirst();
                    titles = new String[resultCursor.getCount()];
                    posters = new String[resultCursor.getCount()];

                    int i = 0;
                    while (!resultCursor.isAfterLast()) {

                        String id = resultCursor.getString(resultCursor.getColumnIndex("id"));
                        String title = resultCursor.getString(resultCursor.getColumnIndex("title"));
                        String poster = resultCursor.getString(resultCursor.getColumnIndex("poster"));
                        String synopsis = resultCursor.getString(resultCursor.getColumnIndex("synopsis"));
                        String rating = resultCursor.getString(resultCursor.getColumnIndex("rating"));
                        String release = resultCursor.getString(resultCursor.getColumnIndex("release"));
                        String BASE_URL = "http://image.tmdb.org/t/p/";
                        String SIZE_PARAM = "w342/";
                        Uri uri = Uri.parse(BASE_URL).buildUpon()
                                .appendEncodedPath(SIZE_PARAM)
                                .appendEncodedPath(poster)
                                .build();
                        movies.add(new Movie(id, title, poster, rating, release, synopsis));
                        titles[i] = title;
                        posters[i] = poster;
                        i++;
                        resultCursor.moveToNext();
                    }

                    MyAdapter adapter = new MyAdapter(getActivity(), titles, posters);
                    gridView.setAdapter(adapter);
                    gridView.refreshDrawableState();
                } else {
                    Toast.makeText(getActivity(), "UhOH! You do not have any Movies as your Favourites yet", Toast.LENGTH_LONG).show();
                }

            } else if (!flag) {
                new fetchMovies().execute(sort_order);

            } else {
                if (movies.size() != 0) {
                    titles = new String[movies.size()];
                    posters = new String[movies.size()];
                    for (int i = 0; i < movies.size(); i++) {
                        titles[i] = movies.get(i).getTitle();
                        posters[i] = movies.get(i).getPoster();
                    }
                    gridView.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.INVISIBLE);
                    btn.setVisibility(View.INVISIBLE);
                    MyAdapter adapter = new MyAdapter(getActivity(), titles, posters);
                    gridView.setAdapter(adapter);
                    gridView.refreshDrawableState();
                }
            }


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment2) == null) {
                        Intent i = new Intent(getActivity(), MovieDetailsActivity.class);
                        i.putExtra("id", movies.get(position).getId());
                        i.putExtra("title", movies.get(position).getTitle());
                        i.putExtra("poster", movies.get(position).getPoster());
                        i.putExtra("synopsis", movies.get(position).getSynopsis());
                        i.putExtra("rating", movies.get(position).getRating());
                        i.putExtra("release", movies.get(position).getRelease());
                        startActivity(i);
                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putString("id", movies.get(position).getId());
                        bundle.putString("title", movies.get(position).getTitle());
                        bundle.putString("poster", movies.get(position).getPoster());
                        bundle.putString("synopsis", movies.get(position).getSynopsis());
                        bundle.putString("rating", movies.get(position).getRating());
                        bundle.putString("release", movies.get(position).getRelease());

                        MovieDetailsActivityFragment fragment = new MovieDetailsActivityFragment();
                        fragment.setArguments(bundle);


                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment2, fragment, "DETAILS_FRAGMENT")
                                .commit();
                    }
                }
            });

        } else {
            Toast.makeText(getActivity(), "API Key is Missing.\n" +
                    "Please add a valid key in tmdb.java and recompile", Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }

    class fetchMovies extends AsyncTask<String, Void, String> {

        private ProgressDialog progressdialog;
        private String LOG_TAG = this.getClass().getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String title = getResources().getString(R.string.progress_title);
            String message = getResources().getString(R.string.progress_message);

            progressdialog = ProgressDialog.show(getActivity(), title, message);

        }


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonResponse = null;
            StringBuffer stringBuffer;

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

//                Log.d(LOG_TAG, "JSON RESPONSE:\n" + jsonResponse);


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

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);

            if (jsonResponse != null) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    titles = new String[jsonArray.length()];
                    posters = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject movie = jsonArray.getJSONObject(i);

                        String BASE_URL = "http://image.tmdb.org/t/p/";
                        String SIZE_PARAM = "w342/";
                        Uri uri = Uri.parse(BASE_URL).buildUpon()
                                .appendEncodedPath(SIZE_PARAM)
                                .appendEncodedPath(movie.getString("poster_path"))
                                .build();

                        movies.add(new Movie(movie.getString("id"), movie.getString("title"), uri.toString(),
                                movie.getString("vote_average"), movie.getString("release_date"),
                                movie.getString("overview")));

                        titles[i] = movies.get(i).getTitle();
                        posters[i] = movies.get(i).getPoster();
//                        Log.d(LOG_TAG, movies.get(i).toString());
                    }


                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.toString());
                }

                MyAdapter adapter = new MyAdapter(getActivity(), titles, posters);
                gridView.setAdapter(adapter);
                gridView.refreshDrawableState();

            } else {
                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
            }

            progressdialog.dismiss();
        }

    }


}
