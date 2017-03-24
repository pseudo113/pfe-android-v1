package com.labrosse.suivicommercial.service.response.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.AdUser;

import java.util.ArrayList;

/**
 * Created by ahmedhammami on 07/01/2017.
 */

public class AdUserResponse {

    @SerializedName("users")
    @Expose
    private ArrayList<AdUser> users = null;

    @SerializedName("erreur")
    @Expose
    private String erreur;

    public ArrayList<AdUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<AdUser> users) {
        this.users = users;
    }

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }

}
