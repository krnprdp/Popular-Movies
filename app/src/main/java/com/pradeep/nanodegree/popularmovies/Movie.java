package com.pradeep.nanodegree.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pradeep on 8/10/15.
 *
 * This is a custom class for storing Movies Information as a single object. It implements android.os.parcelable
 * interface for persisting orientation changes so that data need not be fetched from the net always.
 *
 * Each object of this class holds all information about a particular Movie obtained in the JSON Response from
 * themoviedb.org API Call.
 *
 */
public class Movie implements Parcelable {
    String id, title, poster, rating, release, synopsis;

    public Movie(String id, String title, String poster, String rating, String release, String synopsis) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.rating = rating;
        this.release = release;
        this.synopsis = synopsis;
    }

    private Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster = in.readString();
        rating = in.readString();
        release = in.readString();
        synopsis = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getRating() {
        return rating;
    }

    public String getRelease() {
        return release;
    }

    public String getSynopsis() {
        return synopsis;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", rating='" + rating + '\'' +
                ", release='" + release + '\'' +
                ", synopsis='" + synopsis + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(rating);
        dest.writeString(release);
        dest.writeString(synopsis);
    }
}
