/*
 * Created By Amit Patel
 * Project 1: Popular Movies
 * For the Udacity Nanodegree
 */
package ajpatel72.udacitypopularmovies1.model;

import java.util.List;

/**
 * Simple Java Class that will hold the individual Movie Objects
 */
public class MovieResponse {

    public final List<Movie> results;

    public MovieResponse(List<Movie> results) {
        this.results = results;
    }
}
