package com.labrosse.suivicommercial.service.response.map.direction;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmedhammami on 03/01/2017.
 */

public class DirectionResults {
    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }}

