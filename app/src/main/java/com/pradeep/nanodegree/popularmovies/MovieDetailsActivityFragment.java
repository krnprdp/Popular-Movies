package com.pradeep.nanodegree.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        tvTitle.setText(title);
        tvRelease.setText("Release Date: " + release);
        tvRating.setText("Rating: " + rating + "/10");
        tvSynopsis.setText("Synopsis:\n" + synopsis);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.ivPoster);

        Picasso.with(getActivity()).load(poster).into(ivPoster);


        return rootView;
    }
}
