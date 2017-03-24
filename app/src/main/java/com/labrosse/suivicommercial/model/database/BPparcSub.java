package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmedhammami on 31/12/2016.
 */

public class BPparcSub implements Parcelable{

    @SerializedName("c_bpparc_sub_id")
    @Expose
    protected int c_bpparc_sub_id;

    @SerializedName("value")
    @Expose
    protected  String value;

    @SerializedName("c_bpparcours_id")
    @Expose
    protected int c_bpparcours_id;

    @SerializedName("pvalue")
    @Expose
    protected  String parcours_value;


    @SerializedName("c_bpsub_id")
    @Expose
    protected int c_bpsub_id;

    @SerializedName("seq")
    @Expose
    protected int seq;

    protected transient String synchronised;

    public BPparcSub() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getC_bpparc_sub_id() {
        return c_bpparc_sub_id;
    }

    public void setC_bpparc_sub_id(int c_bpparc_sub_id) {
        this.c_bpparc_sub_id = c_bpparc_sub_id;
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

    public String getSynchronised() {
        return synchronised;
    }

    public void setSynchronised(String synchronised) {
        this.synchronised = synchronised;
    }

    public String getParcours_value() {
        return parcours_value;
    }

    public void setParcours_value(String parcours_value) {
        this.parcours_value = parcours_value;
    }


    protected BPparcSub(Parcel in) {
        c_bpparc_sub_id = in.readInt();
        value = in.readString();
        c_bpparcours_id = in.readInt();
        c_bpsub_id = in.readInt();
        seq = in.readInt();
        synchronised = in.readString();
        parcours_value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(c_bpparc_sub_id);
        dest.writeString(value);
        dest.writeInt(c_bpparcours_id);
        dest.writeInt(c_bpsub_id);
        dest.writeInt(seq);
        dest.writeString(synchronised);
        dest.writeString(parcours_value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BPparcSub> CREATOR = new Creator<BPparcSub>() {
        @Override
        public BPparcSub createFromParcel(Parcel in) {
            return new BPparcSub(in);
        }

        @Override
        public BPparcSub[] newArray(int size) {
            return new BPparcSub[size];
        }
    };
}
