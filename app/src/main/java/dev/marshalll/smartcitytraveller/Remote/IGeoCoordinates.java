package dev.marshalll.smartcitytraveller.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface IGeoCoordinates {
    @GET("maps/api/geocode/json")
    Call<String> getGeoCode(@Query("address") String location);

    @GET("maps/api/directions/json")
    Call<String> getDirections(@Query("origin") String origin, @Query("destination") String destination);

}
