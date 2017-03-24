package com.labrosse.suivicommercial.model.database;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class MProduct implements Parcelable {

    @SerializedName("c_bpsub_product_id")
    private  int c_bpsub_product_id ;

    @SerializedName("c_bpsub_id")
    private  int c_bpsub_id ;

    @SerializedName("m_product_id")
    private  int m_product_id ;

    @SerializedName("productname")
    private  String productname ;

    @SerializedName("productvalue")
    private  String productvalue ;

    @SerializedName("qtymin")
    private  int qty_min ;

    private Date synchronisedate ;

    public MProduct(String productname) {
        this.productname = productname;
    }

    public MProduct() {

    }

    public int getC_bpsub_product_id() {
        return c_bpsub_product_id;
    }

    public void setC_bpsub_product_id(int c_bpsub_product_id) {
        this.c_bpsub_product_id = c_bpsub_product_id;
    }

    public int getC_bpsub_id() {
        return c_bpsub_id;
    }

    public void setC_bpsub_id(int c_bpsub_id) {
        this.c_bpsub_id = c_bpsub_id;
    }

    public int getM_product_id() {
        return m_product_id;
    }

    public void setM_product_id(int m_product_id) {
        this.m_product_id = m_product_id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductvalue() {
        return productvalue;
    }

    public void setProductvalue(String productvalue) {
        this.productvalue = productvalue;
    }

    public int getQty_min() {
        return qty_min;
    }

    public void setQty_min(int qty_min) {
        this.qty_min = qty_min;
    }

    public Date getSynchronisedate() {
        return synchronisedate;
    }

    public void setSynchronisedate(String synchronisedate) {
        this.synchronisedate = null;
    }

    protected MProduct(Parcel in) {
        c_bpsub_product_id = in.readInt();
        c_bpsub_id = in.readInt();
        m_product_id = in.readInt();
        productname = in.readString();
        productvalue = in.readString();
        qty_min = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(c_bpsub_product_id);
        dest.writeInt(c_bpsub_id);
        dest.writeInt(m_product_id);
        dest.writeString(productname);
        dest.writeString(productvalue);
        dest.writeInt(qty_min);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MProduct> CREATOR = new Creator<MProduct>() {
        @Override
        public MProduct createFromParcel(Parcel in) {
            return new MProduct(in);
        }

        @Override
        public MProduct[] newArray(int size) {
            return new MProduct[size];
        }
    };
}
