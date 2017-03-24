package com.labrosse.suivicommercial.model.database;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class SubBPartner implements Parcelable {

    @SerializedName("c_bpsub_id")
    private  int c_bpsub_id ;

    @SerializedName("value")
    private  String value ;

    @SerializedName("name")
    private  String name ;

    @SerializedName("c_city_id")
    private  int c_city_id ;

    @SerializedName("adresse")
    private  String adresse ;

    @SerializedName("gps")
    private  String gps ;

    private  Date synchroniseDate ;
    private  boolean isSelected;


    public SubBPartner() {
    }

    public int getC_bpsub_id() {
        return c_bpsub_id;
    }

    public void setC_bpsub_id(int c_bpsub_id) {
        this.c_bpsub_id = c_bpsub_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getC_city_id() {
        return c_city_id;
    }

    public void setC_city_id(int c_city_id) {
        this.c_city_id = c_city_id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public Date getSynchroniseDate() {
        return synchroniseDate;
    }

    public void setSynchroniseDate(Date synchronisedate) {
        this.synchroniseDate = synchronisedate;
    }

    protected SubBPartner(Parcel in) {
        c_bpsub_id = in.readInt();
        value = in.readString();
        name = in.readString();
        c_city_id = in.readInt();
        adresse = in.readString();
        gps = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(c_bpsub_id);
        dest.writeString(value);
        dest.writeString(name);
        dest.writeInt(c_city_id);
        dest.writeString(adresse);
        dest.writeString(gps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubBPartner> CREATOR = new Creator<SubBPartner>() {
        @Override
        public SubBPartner createFromParcel(Parcel in) {
            return new SubBPartner(in);
        }

        @Override
        public SubBPartner[] newArray(int size) {
            return new SubBPartner[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        SubBPartner sub = (SubBPartner) obj;
        if(this.getC_bpsub_id() == sub.getC_bpsub_id() && this.getC_city_id() == sub.getC_city_id()){
            return true;
        }else {
            return false;
        }

    }
}
