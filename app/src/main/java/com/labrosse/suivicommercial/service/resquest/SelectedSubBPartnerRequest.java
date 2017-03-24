package com.labrosse.suivicommercial.service.resquest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.BPparcSub;

public class SelectedSubBPartnerRequest  extends BPparcSub{

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("ad_user_id")
    @Expose
    private Integer adUserId;

    public SelectedSubBPartnerRequest(BPparcSub bPparcSub) {
        this.c_bpparc_sub_id = bPparcSub.getC_bpparc_sub_id();
        this.value = bPparcSub.getValue();
        this.c_bpparcours_id = bPparcSub.getC_bpparcours_id();
        this.c_bpsub_id = bPparcSub.getC_bpsub_id();
        this.seq = bPparcSub.getSeq();
        this.synchronised = bPparcSub.getSynchronised();
        this.parcours_value = bPparcSub.getParcours_value();
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
