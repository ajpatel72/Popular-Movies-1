/*
 * Created By Amit Patel
 * Project 1: Popular Movies
 * For the Udacity Nanodegree
 */
package ajpatel72.udacitypopularmovies1.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import ajpatel72.udacitypopularmovies1.R;
import ajpatel72.udacitypopularmovies1.model.Movie;
import ajpatel72.udacitypopularmovies1.utils.ImageUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    static final String EXTRA_MOVIE = "MOVIE";
    Movie mMovie;

    @Bind(R.id.toolbar)             Toolbar mToolbar;
    @Bind(R.id.title)               TextView mTitle;
    @Bind(R.id.imagePoster)         ImageView mImagePoster;
    @Bind(R.id.release_date)        TextView mTvReleaseDate;
    @Bind(R.id.plot)                TextView mTvPlot;
    @Bind(R.id.rating)              TextView mTvRating;
    @Bind(R.id.backdrop)            ImageView mImageBackDrop;
    @Bind(R.id.collapsing_toolbar)  CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        loadFromIntent();

        setupToolbar();

        loadBackdrop();

        loadUI();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbar.setTitle(mMovie.original_title);
    }

    /**
     * This method loads the display widgets with their related
     * text/images from the movie object.
     *
     * Null checks are done for display and performance reasons
     */
    private void loadUI() {
        if(mMovie.poster_path != null) {
            Picasso.with(this)
                    .load(ImageUtils.getPosterPath(mMovie, getString(R.string.param_poster_size)))
                    .into(mImagePoster, new Callback() {
                        @Override
                        public void onSuccess() {
                            updateBackgroundColor();
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
        if (mMovie.original_title != null) {
            mTitle.setText(mMovie.original_title);
        } else{
            mTvPlot.setText("Title Not Available.");
        }

        if (mMovie.vote_average < 0.0F) {
            mTvPlot.setText("Rating Not Available.");
        } else {
            String ratingText = "Average Rating: " + mMovie.vote_average + "/10";
            mTvRating.setText(ratingText);
        }

        String getMovieString = mMovie.release_date;
        if (getMovieString != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String dateToFormat = DateUtils.formatDateTime(this,
                        formatter.parse(getMovieString).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                mTvReleaseDate.setText(dateToFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            mTvReleaseDate.setText("Year Not Available.");
        }

        if (mMovie.overview != null) {
            mTvPlot.setText(mMovie.overview);
        } else {
            mTvPlot.setText("Plot Not Available.");
        }
    }

    /**
     * Make use of the Pallete object to add funky backgrounds :)
     */
    private void updateBackgroundColor() {
        Bitmap bitmap = ImageUtils.getBitmap(mImagePoster);
        Palette palette = Palette.from(bitmap).generate();
        int colorStart = palette.getDarkMutedColor(R.color.primary);
        int colorEnd = palette.getDarkVibrantColor(R.color.accent);
        GradientDrawable gradientDrawable =
                new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[]{colorStart, colorEnd});
        gradientDrawable.setDither(true);
        this.getWindow().setBackgroundDrawable(gradientDrawable);
    }

    private void loadBackdrop() {
        if(mMovie.backdrop_path != null) {
            Picasso.with(this)
                    .load(ImageUtils.getBackdropPath(mMovie, getString(R.string.param_backdrop_size)))
                    .into(mImageBackDrop);
        }
    }

    /**
     * Get the intent from the previous activity.
     * Load the movie object
     */
    public void loadFromIntent() {
        Intent newIntent = getIntent();
        if (newIntent == null) {
            finish();
            return;
        }
        mMovie = newIntent.getParcelableExtra(EXTRA_MOVIE);
        if (mMovie == null) {
            finish();
            return;
        }
    }

    /**
     * Preferred way of building an intent to start a new activity
     * and pass in information via intent extras.
     */
    public static Intent buildIntent(Activity activity, Movie movie) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

}
