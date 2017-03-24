package com.labrosse.suivicommercial.service.response.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.City;

import java.util.ArrayList;

/**
 * Created by ahmedhammami on 07/01/2017.
 */

public class CityResponse {

    @SerializedName("erreur")
    @Expose
    private String erreur;
    @SerializedName("cities")
    @Expose
    private ArrayList<City> cities = null;

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

}
