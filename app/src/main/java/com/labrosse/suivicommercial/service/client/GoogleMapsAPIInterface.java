package com.labrosse.suivicommercial.service.client;


import com.labrosse.suivicommercial.service.response.map.direction.DirectionResults;
import com.labrosse.suivicommercial.service.response.map.distance.DirectionModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ahmedhammami on 03/01/2017.
 */

public interface GoogleMapsAPIInterface {

    @GET("directions/json")
    Call<DirectionResults> getDirectionJson(@Query("origin") String origin, @Query("destination") String destination, @Query("waypoints") String waypoint, @Query("key") String key);

    @GET("distancematrix/json")
    Call<DirectionModel> getDistanceJson(@Query("origins") String origin, @Query("destinations") String destination , @Query("key") String key);
}
