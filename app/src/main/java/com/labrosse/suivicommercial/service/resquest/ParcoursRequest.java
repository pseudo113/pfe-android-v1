package com.labrosse.suivicommercial.service.resquest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.BPparcours;

public class ParcoursRequest extends BPparcours implements Cloneable{

    @SerializedName("token")
    @Expose
    private String token;

    public ParcoursRequest(BPparcours bPparcours) {
        this.c_bpparcours_id = bPparcours.getC_bpparcours_id();
        this.value = bPparcours.getValue();
        this.ad_user_id = bPparcours.getAd_user_id();
        this.startDate = bPparcours.getStartDate();
        this.endDate = bPparcours.getEndDate();
        this.processing = bPparcours.getProcessing();
        this.stat = bPparcours.getStat();
        this.gpsStartReal = bPparcours.getGpsStartReal();
        this.gpsStartTheoric = bPparcours.getGpsStartTheoric();
        this.kmTheoricProposed = bPparcours.getKmTheoricProposed();
        this.kmTheoricDone = bPparcours.getKmTheoricDone();
        this.kmRealDone = bPparcours.getKmRealDone();
        this.synchronised = bPparcours.getSynchronised();
    }

    public ParcoursRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
