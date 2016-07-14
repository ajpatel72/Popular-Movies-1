/*
 * Created By Amit Patel
 * Project 1: Popular Movies
 * For the Udacity Nanodegree
 */
package ajpatel72.udacitypopularmovies1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

import ajpatel72.udacitypopularmovies1.R;
import ajpatel72.udacitypopularmovies1.model.Movie;
import ajpatel72.udacitypopularmovies1.utils.ImageUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * An adapter that is specially configured to be used with a RecyclerView
 * Most important Methods are onCreateViewHolder that inflate the view and
 * onBindViewHolder where the binding is done at each pass.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private final List<Movie> mMovies;
    private Context mContext;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, final int position) {
        final Movie movie = mMovies.get(position);
        String url = ImageUtils.getPosterPath(movie, mContext.getString(R.string.param_poster_size));
        if (movie.poster_path != null) {
            Picasso.with(mContext)
                    .load(url)
                    .placeholder(R.color.placeholder)
                    .into(movieViewHolder.photo);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    public void add(Collection<Movie> movie) {
        mMovies.addAll(movie);
        notifyDataSetChanged();
    }

    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.scalingPhoto)
        ImageView photo;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}



