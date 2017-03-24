package com.labrosse.suivicommercial.service.response.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmedhammami on 07/01/2017.
 */

public class LoginResponse {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("erreur")
    @Expose
    private String erreur;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }

}
