package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahmedhammami on 18/12/2016.
 */

public class ExtendedBPparcVisiteProduct implements Parcelable{

    private  long c_bpparc_visit_prod;
    private  long c_bpparc_visit;
    private  int m_product_id;
    private  String product_name;
    private  String product_value;
    private  int qty;


    public ExtendedBPparcVisiteProduct() {
    }


    protected ExtendedBPparcVisiteProduct(Parcel in) {
        c_bpparc_visit_prod = in.readLong();
        c_bpparc_visit = in.readLong();
        m_product_id = in.readInt();
        product_name = in.readString();
        product_value = in.readString();
        qty = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(c_bpparc_visit_prod);
        dest.writeLong(c_bpparc_visit);
        dest.writeInt(m_product_id);
        dest.writeString(product_name);
        dest.writeString(product_value);
        dest.writeInt(qty);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExtendedBPparcVisiteProduct> CREATOR = new Creator<ExtendedBPparcVisiteProduct>() {
        @Override
        public ExtendedBPparcVisiteProduct createFromParcel(Parcel in) {
            return new ExtendedBPparcVisiteProduct(in);
        }

        @Override
        public ExtendedBPparcVisiteProduct[] newArray(int size) {
            return new ExtendedBPparcVisiteProduct[size];
        }
    };

    public long getC_bpparc_visit_prod() {
        return c_bpparc_visit_prod;
    }

    public void setC_bpparc_visit_prod(long c_bpparc_visit_prod) {
        this.c_bpparc_visit_prod = c_bpparc_visit_prod;
    }

    public long getC_bpparc_visit() {
        return c_bpparc_visit;
    }

    public void setC_bpparc_visit(long c_bpparc_visit) {
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_value() {
        return product_value;
    }

    public void setProduct_value(String product_value) {
        this.product_value = product_value;
    }
}
