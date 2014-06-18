package egorand.rxjavaandroiddemo.rest;

import egorand.rxjavaandroiddemo.model.BeerContainer;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface BeersRestClient {

    @GET("/beers/?key=e2ba485fad3c0d5e5037d8de04f3d6ef&format=json") void getBeers(
            @Query("styleId") String styleId,
            Callback<BeerContainer> cb);
}
