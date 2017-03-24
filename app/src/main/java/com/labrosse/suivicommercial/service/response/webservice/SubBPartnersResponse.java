package com.labrosse.suivicommercial.service.response.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.SubBPartner;

import java.util.ArrayList;

/**
 * Created by ahmedhammami on 07/01/2017.
 */

public class SubBPartnersResponse {

    @SerializedName("erreur")
    @Expose
    private String erreur;

    @SerializedName("magasins")
    @Expose
    private ArrayList<SubBPartner> subBPartners = null;

    public ArrayList<SubBPartner> getSubBPartners() {
        return subBPartners;
    }

    public void setSubBPartners(ArrayList<SubBPartner> subBPartners) {
        this.subBPartners = subBPartners;
    }

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }
}
