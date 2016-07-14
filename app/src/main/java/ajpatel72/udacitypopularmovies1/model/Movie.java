/*
 * Created By Amit Patel
 * Project 1: Popular Movies
 * For the Udacity Nanodegree
 */
package ajpatel72.udacitypopularmovies1.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Simple POJO that implements Parcelable so it can be passed
 * from on activity to another via the bundle functionality.
 */
public class Movie implements Parcelable {

    public final String id;
    public final String original_title;
    public final String poster_path;
    public final String backdrop_path;
    public final String overview;
    public final float  vote_average;
    public final String release_date;

    protected Movie(Parcel in) {
        id                   = in.readString();
        original_title       = in.readString();
        poster_path          = in.readString();
        backdrop_path        = in.readString();
        overview             = in.readString();
        vote_average         = in.readFloat();
        release_date         = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeFloat(vote_average);
        dest.writeString(release_date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
