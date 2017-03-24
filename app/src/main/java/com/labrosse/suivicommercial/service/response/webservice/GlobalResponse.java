package com.labrosse.suivicommercial.service.response.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 14/01/2017.
 */

public class GlobalResponse {

    @SerializedName("erreur")
    @Expose
    private String erreur;

    @SerializedName("ok")
    @Expose
    private String ok;

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }
}
