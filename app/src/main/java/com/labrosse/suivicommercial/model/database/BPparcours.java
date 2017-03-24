package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BPparcours implements Parcelable{

    @SerializedName("c_bpparcours_id")
    @Expose
    protected  long c_bpparcours_id;

    @SerializedName("value")
    @Expose
    protected  String value;

    @SerializedName("ad_user_id")
    @Expose
    protected  int ad_user_id;

    @SerializedName("startdate")
    @Expose
    protected  String startDate;

    @SerializedName("enddate")
    @Expose
    protected  String endDate;

    @SerializedName("processing")
    @Expose
    protected  String processing;

    @SerializedName("stat")
    @Expose
    protected  String stat;

    @SerializedName("gpsstartreal")
    @Expose
    protected  String gpsStartReal;

    @SerializedName("gpsstartthe")
    @Expose
    protected  String gpsStartTheoric;

    @SerializedName("kmtheproposed")
    @Expose
    protected  int kmTheoricProposed;

    @SerializedName("kmthedo")
    @Expose
    protected  int kmTheoricDone;

    @SerializedName("kmrealdo")
    @Expose
    protected  int kmRealDone;

    protected transient String synchronised;

    public BPparcours() {
    }

    public long getC_bpparcours_id() {
        return c_bpparcours_id;
    }

    public void setC_bpparcours_id(long c_bpparcours_id) {
        this.c_bpparcours_id = c_bpparcours_id;
    }

    public int getAd_user_id() {
        return ad_user_id;
    }

    public void setAd_user_id(int ad_user_id) {
        this.ad_user_id = ad_user_id;
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

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getGpsStartReal() {
        return gpsStartReal;
    }

    public void setGpsStartReal(String gpsStartReal) {
        this.gpsStartReal = gpsStartReal;
    }

    public String getGpsStartTheoric() {
        return gpsStartTheoric;
    }

    public void setGpsStartTheoric(String gpsStartTheoric) {
        this.gpsStartTheoric = gpsStartTheoric;
    }

    public int getKmTheoricProposed() {
        return kmTheoricProposed;
    }

    public void setKmTheoricProposed(int kmTheoricProposed) {
        this.kmTheoricProposed = kmTheoricProposed;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKmTheoricDone() {
        return kmTheoricDone;
    }

    public void setKmTheoricDone(int kmTheoricDone) {
        this.kmTheoricDone = kmTheoricDone;
    }

    public int getKmRealDone() {
        return kmRealDone;
    }

    public void setKmRealDone(int kmRealDone) {
        this.kmRealDone = kmRealDone;
    }

    public String getSynchronised() {
        return synchronised;
    }

    public void setSynchronised(String synchronised) {
        this.synchronised = synchronised;
    }

    protected BPparcours(Parcel in) {
        c_bpparcours_id = in.readLong();
        value = in.readString();
        ad_user_id = in.readInt();
        processing = in.readString();
        stat = in.readString();
        gpsStartReal = in.readString();
        gpsStartTheoric = in.readString();
        kmTheoricProposed = in.readInt();
        kmTheoricDone = in.readInt();
        kmRealDone = in.readInt();
        synchronised = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(c_bpparcours_id);
        dest.writeString(value);
        dest.writeInt(ad_user_id);
        dest.writeString(processing);
        dest.writeString(stat);
        dest.writeString(gpsStartReal);
        dest.writeString(gpsStartTheoric);
        dest.writeInt(kmTheoricProposed);
        dest.writeInt(kmTheoricDone);
        dest.writeInt(kmRealDone);
        dest.writeString(synchronised);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BPparcours> CREATOR = new Creator<BPparcours>() {
        @Override
        public BPparcours createFromParcel(Parcel in) {
            return new BPparcours(in);
        }

        @Override
        public BPparcours[] newArray(int size) {
            return new BPparcours[size];
        }
    };
}
