package com.labrosse.suivicommercial.service.response.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.MProduct;

import java.util.ArrayList;

/**
 * Created by ahmedhammami on 07/01/2017.
 */

public class ProductResponse {

    @SerializedName("erreur")
    @Expose
    private String erreur;

    @SerializedName("products")
    @Expose
    private ArrayList<MProduct> mProducts = null;

    public ArrayList<MProduct> getProducts() {
        return mProducts;
    }

    public void setProducts(ArrayList<MProduct> mProducts) {
        this.mProducts = mProducts;
    }

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }
}
