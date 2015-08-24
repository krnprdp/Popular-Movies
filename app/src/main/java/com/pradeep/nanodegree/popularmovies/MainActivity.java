package com.pradeep.nanodegree.popularmovies;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/*
 * @author: Pradeep Kiran Chakkirala
 *          krnprdp (at) gmail (dot) com
  *
 * MainActivity Loads the PopularMoviesFragment and also the MovieDetailsFragment.
 *
 * Please do not forget to add the API Key in tmdb.java More information can be seen in that class.
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            PopularMoviesFragment fragment = new PopularMoviesFragment();

            Bundle bundle = new Bundle();
            bundle.putString("api_key", tmdb.getKey());
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        String sortOrder = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popularity) {
            sortOrder = "POPULARITY";
            PopularMoviesFragment fragment = new PopularMoviesFragment();

            Bundle bundle = new Bundle();
            bundle.putString("api_key", tmdb.getKey());
            bundle.putString("sort_order", sortOrder);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        }
        if (id == R.id.action_rating) {
            sortOrder = "vote_count";
            PopularMoviesFragment fragment = new PopularMoviesFragment();

            Bundle bundle = new Bundle();
            bundle.putString("api_key", tmdb.getKey());
            bundle.putString("sort_order", sortOrder);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
