package com.pradeep.nanodegree.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pradeep on 8/9/15.
 */
public class PopularMoviesFragment extends Fragment {
    String api_key;

    public PopularMoviesFragment() {
        Bundle args = getArguments();
        this.api_key = args.getString("api_key");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popularmovies, container, false);
        return rootView;
    }
}
