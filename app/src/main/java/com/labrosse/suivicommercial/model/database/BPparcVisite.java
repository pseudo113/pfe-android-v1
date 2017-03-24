package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmedhammami on 17/12/2016.
 */

public class BPparcVisite implements Parcelable{

    @SerializedName("c_bpparc_visit_id")
    @Expose
    protected  int c_bpparc_visit_id;

    @SerializedName("value")
    @Expose
    protected  String value;

    @SerializedName("c_bpparcours_id")
    @Expose
    protected  int c_bpparcours_id;

    @SerializedName("c_bpsub_id")
    @Expose
    protected  int c_bpsub_id;

    @SerializedName("seq")
    @Expose
    protected  int seq;

    @SerializedName("startdate")
    @Expose
    protected  String startDate;

    @SerializedName("enddate")
    @Expose
    protected  String endDate;

    @SerializedName("pvalue")
    @Expose
    protected String parcours_value;

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    @SerializedName("gps")
    @Expose
    protected String gps;

    @SerializedName("processing")
    @Expose
    protected  String processing;

    protected transient String synchronised;

    public BPparcVisite() {
    }

    public int getC_bpparc_visit_id() {
        return c_bpparc_visit_id;
    }

    public void setC_bpparc_visit_id(int c_bpparc_visit_id) {
        this.c_bpparc_visit_id = c_bpparc_visit_id;
    }

    public int getC_bpparcours_id() {
        return c_bpparcours_id;
    }

    public void setC_bpparcours_id(int c_bpparcours_id) {
        this.c_bpparcours_id = c_bpparcours_id;
    }

    public int getC_bpsub_id() {
        return c_bpsub_id;
    }

    public void setC_bpsub_id(int c_bpsub_id) {
        this.c_bpsub_id = c_bpsub_id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
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

    protected BPparcVisite(Parcel in) {
        c_bpparc_visit_id = in.readInt();
        value = in.readString();
        c_bpparcours_id = in.readInt();
        c_bpsub_id = in.readInt();
        seq = in.readInt();
        processing = in.readString();
        synchronised = in.readString();
        parcours_value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(c_bpparc_visit_id);
        dest.writeString(value);
        dest.writeInt(c_bpparcours_id);
        dest.writeInt(c_bpsub_id);
        dest.writeInt(seq);
        dest.writeString(processing);
        dest.writeString(synchronised);
        dest.writeString(parcours_value);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Creator<BPparcVisite> CREATOR = new Creator<BPparcVisite>() {
        @Override
        public BPparcVisite createFromParcel(Parcel in) {
            return new BPparcVisite(in);
        }

        @Override
        public BPparcVisite[] newArray(int size) {
            return new BPparcVisite[size];
        }
    };

    public void setParcours_value(String parcours_value) {
        this.parcours_value = parcours_value;
    }

    public String getParcours_value() {
        return parcours_value;
    }
}
