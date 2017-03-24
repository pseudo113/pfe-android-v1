package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmedhammami on 18/12/2016.
 */

public class BPparcVisiteProduct implements Parcelable{

    @SerializedName("c_bpparc_visit_prod_id")
    @Expose
    protected  int c_bpparc_visit_prod;


    @SerializedName("value")
    @Expose
    protected  String value;

    @SerializedName("c_bpparc_visit_id")
    @Expose
    protected  int c_bpparc_visit;

    @SerializedName("m_product_id")
    @Expose
    protected  int m_product_id;

    @SerializedName("qty")
    @Expose
    protected  int qty;

    protected transient String synchronised;

    @SerializedName("vvalue")
    @Expose
    protected String visitValue;

    public BPparcVisiteProduct() {
    }

    public BPparcVisiteProduct(BPparcVisiteProduct other) {
        this.c_bpparc_visit_prod = other.getC_bpparc_visit_prod();
        this.c_bpparc_visit = other.getC_bpparc_visit();
        this.m_product_id = other.getM_product_id();
        this.qty = other.getQty();
        this.synchronised = other.getSynchronised();
    }

    public int getC_bpparc_visit_prod() {
        return c_bpparc_visit_prod;
    }

    public void setC_bpparc_visit_prod(int c_bpparc_visit_prod) {
        this.c_bpparc_visit_prod = c_bpparc_visit_prod;
    }

    public int getC_bpparc_visit() {
        return c_bpparc_visit;
    }

    public void setC_bpparc_visit(int c_bpparc_visit) {
        this.c_bpparc_visit = c_bpparc_visit;
    }

    public int getM_product_id() {
        return m_product_id;
    }

    public void setM_product_id(int m_product_id) {
        this.m_product_id = m_product_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getSynchronised() {
        return synchronised;
    }

    public void setSynchronised(String synchronised) {
        this.synchronised = synchronised;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    protected BPparcVisiteProduct(Parcel in) {
        c_bpparc_visit_prod = in.readInt();
        value = in.readString();
        c_bpparc_visit = in.readInt();
        m_product_id = in.readInt();
        qty = in.readInt();
        synchronised = in.readString();
        visitValue = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(c_bpparc_visit_prod);
        dest.writeString(value);
        dest.writeInt(c_bpparc_visit);
        dest.writeInt(m_product_id);
        dest.writeInt(qty);
        dest.writeString(synchronised);
        dest.writeString(visitValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BPparcVisiteProduct> CREATOR = new Creator<BPparcVisiteProduct>() {
        @Override
        public BPparcVisiteProduct createFromParcel(Parcel in) {
            return new BPparcVisiteProduct(in);
        }

        @Override
        public BPparcVisiteProduct[] newArray(int size) {
            return new BPparcVisiteProduct[size];
        }
    };

    public void setVisitValue(String visitValue) {
        this.visitValue = visitValue;
    }

    public String getVisitValue() {
        return visitValue;
    }
}
