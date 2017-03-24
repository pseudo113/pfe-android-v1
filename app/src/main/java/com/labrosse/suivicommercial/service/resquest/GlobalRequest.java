package com.labrosse.suivicommercial.service.resquest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmedhammami on 01/01/2017.
 */

public class GlobalRequest {

    @SerializedName("token")
    @Expose

    private String token;
    @SerializedName("ad_user_id")
    @Expose
    private Integer adUserId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(Integer adUserId) {
        this.adUserId = adUserId;
    }

}
