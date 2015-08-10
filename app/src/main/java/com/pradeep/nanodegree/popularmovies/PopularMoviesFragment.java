package com.pradeep.nanodegree.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Pradeep on 8/9/15.
 */
public class PopularMoviesFragment extends Fragment {
    String api_key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.api_key = args.getString("api_key");

        } else
            this.api_key = "NULL ??";
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popularmovies, container, false);
        return rootView;
    }
}
