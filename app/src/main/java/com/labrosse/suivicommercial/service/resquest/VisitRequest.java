package com.labrosse.suivicommercial.service.resquest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.BPparcVisite;

/**
 * Created by ahmedhammami on 07/01/2017.
 */

public class VisitRequest extends BPparcVisite {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("ad_user_id")
    @Expose
    private Integer adUserId;

    public VisitRequest(BPparcVisite other) {
        this.c_bpparc_visit_id = other.getC_bpparc_visit_id();
        this.value = other.getValue();
        this.c_bpparcours_id = other.getC_bpparcours_id();
        this.c_bpsub_id = other.getC_bpsub_id();
        this.seq = other.getSeq();
        this.startDate = other.getStartDate();
        this.endDate = other.getEndDate();
        this.gps = other.getGps();
        this.processing = other.getProcessing();
        this.synchronised = other.getSynchronised();
        this.parcours_value = other.getParcours_value();
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
