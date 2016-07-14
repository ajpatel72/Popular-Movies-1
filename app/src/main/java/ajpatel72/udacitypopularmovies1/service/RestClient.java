/*
 * Created By Amit Patel
 * Project 1: Popular Movies
 * For the Udacity Nanodegree
 */
package ajpatel72.udacitypopularmovies1.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Create a RestClent object with the RestClientService
 *
 * http://blog.robinchutaux.com/blog/a-smart-way-to-use-retrofit/
 */
public class RestClient  implements RequestInterceptor{

    private static final String API_KEY = "INSERT API KEY HERE";

    private RestClientService mRestClientService;

    /**
     * Create the Endpoint and Gson serializer/deserializer.
     * Create an implementation of the API defined by the specified service interface.
     */
    public RestClient() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        GsonConverter gsonConverter = new GsonConverter(gson);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setConverter(gsonConverter)
                .setRequestInterceptor(this)
                .build();

        mRestClientService = restAdapter.create(RestClientService.class);
    }

    /**
     * Add the API KEY as a query parameter
     */
    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
        request.addEncodedQueryParam("api_key", API_KEY);
    }

    /**
     * Getter method for the RestClientService
     */
    public RestClientService getRestClientService() {
        return mRestClientService;
    }
}
