/*
 * Created By Amit Patel
 * Project 1: Popular Movies
 * For the Udacity Nanodegree
 */
package ajpatel72.udacitypopularmovies1.service;

import ajpatel72.udacitypopularmovies1.model.MovieResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Retrofit turns a REST API into a Java Interface
 */
public interface RestClientService {

    @GET("/discover/movie")
    void getMovies(@Query("sort_by") String sortType,
                   Callback<MovieResponse> callback);
}
