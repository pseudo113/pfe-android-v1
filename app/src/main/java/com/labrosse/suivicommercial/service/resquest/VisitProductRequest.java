package com.labrosse.suivicommercial.service.resquest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.labrosse.suivicommercial.model.database.BPparcVisiteProduct;

public class VisitProductRequest  extends BPparcVisiteProduct {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("ad_user_id")
    @Expose
    private Integer adUserId;

    public VisitProductRequest(BPparcVisiteProduct other, String token, Integer adUserId) {
        this.c_bpparc_visit_prod = other.getC_bpparc_visit_prod();
        this.value = other.getValue();
        this.c_bpparc_visit = other.getC_bpparc_visit();
        this.m_product_id = other.getM_product_id();
        this.qty = other.getQty();
        this.synchronised = other.getSynchronised();
        this.token = token;
        this.adUserId = adUserId;
    }

    public VisitProductRequest(BPparcVisiteProduct other) {
        this.c_bpparc_visit_prod = other.getC_bpparc_visit_prod();
        this.value = other.getValue();
        this.c_bpparc_visit = other.getC_bpparc_visit();
        this.m_product_id = other.getM_product_id();
        this.qty = other.getQty();
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
