package com.labrosse.suivicommercial.service.resquest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.BPparcVisitePicture;

/**
 * Created by ahmedhammami on 07/01/2017.
 */

public class VisitPictureRequest extends BPparcVisitePicture {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("ad_user_id")
    @Expose
    private Integer adUserId;

    public VisitPictureRequest(BPparcVisitePicture other) {
        this.c_visit_pic_id = other.getC_visit_pic_id();
        this.c_bpparc_visit_id = other.getC_bpparc_visit_id();
        this.picture = other.getPicture();
        this.pictureName = other.getPictureName();
        this.pictureDate = other.getPictureDate();
        this.synchronised = other.getSynchronised();
        this.visitValue = other.getVisitValue();
    }

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
