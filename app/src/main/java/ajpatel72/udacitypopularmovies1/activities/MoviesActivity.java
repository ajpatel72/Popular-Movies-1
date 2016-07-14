/*
 * Created By Amit Patel
 * Project 1: Popular Movies
 * For the Udacity Nanodegree
 */
package ajpatel72.udacitypopularmovies1.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ajpatel72.udacitypopularmovies1.R;
import ajpatel72.udacitypopularmovies1.adapter.MoviesAdapter;
import ajpatel72.udacitypopularmovies1.model.Movie;
import ajpatel72.udacitypopularmovies1.model.MovieResponse;
import ajpatel72.udacitypopularmovies1.service.RestClient;
import ajpatel72.udacitypopularmovies1.utils.ItemClickSupport;
import ajpatel72.udacitypopularmovies1.utils.Preferences;
import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MoviesActivity extends AppCompatActivity {

    private static final String KEY_RESTORE_POSITION = "restore_position";
    private int mRestorePosition;

    RestClient mMovieDB;
    private MoviesAdapter mMoviesAdapter;

    List<Movie> mSavedInstanceMovies;

    private String mSort;

    public  static final String POPULARITY_DESC   = "popularity.desc";
    public  static final String VOTE_AVERAGE_DESC = "vote_average.desc";
    private static final String STATE_MOVIES      = "state_movies";

    @Bind(R.id.image_grid)                     RecyclerView mRecyclerViewGrid;
    @Bind(android.R.id.empty)                  ProgressBar mEmpty;
    @Bind(R.id.toolbar)                        Toolbar mToolbar;
    @BindInt(R.integer.photo_grid_columns)            int mColumns;
    @BindDimen(R.dimen.grid_item_spacing)                            int mGridSpacing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);

        // Movie Category from Shared Preferences
        mSort = Preferences.getSortType(this);

        mMovieDB = new RestClient();

        if (savedInstanceState != null) {
            // Maintain scroll position
            mRestorePosition = savedInstanceState.getInt(KEY_RESTORE_POSITION);
            mRecyclerViewGrid.scrollToPosition(mRestorePosition);
            // Hide the progress bar as grid is loaded
            mEmpty.setVisibility(View.GONE);
            // Retrieve saved Movies from the Bundle and save them.
            mSavedInstanceMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
        } else {
            mSavedInstanceMovies = new ArrayList<>();
            // Initial Network Call to load movies
            getMoviesFromTMDB();
        }

        mMoviesAdapter = new MoviesAdapter(this, mSavedInstanceMovies);
        mRecyclerViewGrid.setAdapter(mMoviesAdapter);

        setUpToolbar();

        setUpRecyclerViewGrid();

        setUpDetailsActivity();

    }

    private void setUpToolbar(){
        mToolbar.setSubtitle("Most Popular");
        setSupportActionBar(mToolbar);
    }

    /**
     * Using the ItemClickSupport class, add a Click Listener
     * to each item of the Recyclerview that will start the
     * Details Activity passing the movie information along.
     */
    private void setUpDetailsActivity() {

        ItemClickSupport.addTo(mRecyclerViewGrid).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        MoviesActivity.this.startActivity(
                                DetailsActivity.buildIntent(MoviesActivity.this,
                                        mMoviesAdapter.getItem(position)));
                    }
                }
        );
    }

    /**
     * Save the position of the firstVisibleItem
     * Save the movies as an arraylist and pass them into the bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = ((GridLayoutManager) mRecyclerViewGrid.getLayoutManager()).findFirstVisibleItemPosition();
        if(position > 0) {
            outState.putInt(KEY_RESTORE_POSITION, position);
        }
        outState.putParcelableArrayList(STATE_MOVIES, (ArrayList<? extends Parcelable>) mSavedInstanceMovies);
    }

    /**
     * Create GridLayoutManager and setup the Recyclerview
     */
    private void setUpRecyclerViewGrid() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, mColumns);
        mRecyclerViewGrid.setLayoutManager(gridLayoutManager);
        mRecyclerViewGrid.addItemDecoration(new GridMarginDecoration(mGridSpacing));
        mRecyclerViewGrid.setHasFixedSize(true);
    }


    private static class GridMarginDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public GridMarginDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.top = space;
            outRect.right = space;
            outRect.bottom = space;
        }
    }

    /**
     * Network call that uses Retrofit and uses Shared Preferences to
     * specify the category of movies that are returned.
     * On return, the getPopularMoviesCallback is fired.
     */
    private void getMoviesFromTMDB() {
        mMovieDB.getRestClientService().getMovies(Preferences.getSortType(this), getPopularMoviesCallback);
    }

    /**
     * Retrofit Callback that returns either with a success or failure.
     * On success, the movies are loaded into the grid.
     */
    retrofit.Callback<MovieResponse> getPopularMoviesCallback = new retrofit.Callback<MovieResponse>() {

        @Override
        public void failure(RetrofitError error) {
        }

        @Override
        public void success(MovieResponse movieResponse, Response response) {
            loadMoviesIntoGrid(movieResponse.results);
        }
    };

    /**
     * Hide the progress bar and refresh the view
     */
    private void loadMoviesIntoGrid(List<Movie> results) {
        mEmpty.setVisibility(View.GONE);
        mMoviesAdapter.clear();
        mMoviesAdapter.add(results);
    }

    /**
     * Inflate the menu
     * this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    /**
     * Using the mSort variable that is filled using Shared
     * Preferences, we check the appropriate menu item.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mSort) {
            case POPULARITY_DESC:
                menu.findItem(R.id.menu_sort_popularity).setChecked(true);
                break;
            case VOTE_AVERAGE_DESC:
                menu.findItem(R.id.menu_sort_rating).setChecked(true);
        }
        return true;
    }

    /**
     * When a new menu item is clicked, the Shared Preferences
     * are updated, the Action Bar's subtitle is updated and
     * onSortChange is called to retrieve the movies
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_sort_popularity:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(!item.isChecked());
                    Preferences.setSortType(this, POPULARITY_DESC);
                    getSupportActionBar().setSubtitle("Most Popular");
                    onSortChanged();
                }
                break;

            case R.id.menu_sort_rating:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(!item.isChecked());
                    Preferences.setSortType(this, VOTE_AVERAGE_DESC);
                    getSupportActionBar().setSubtitle("Highest Rated");
                    onSortChanged();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSortChanged() {
        getMoviesFromTMDB();
    }
}
