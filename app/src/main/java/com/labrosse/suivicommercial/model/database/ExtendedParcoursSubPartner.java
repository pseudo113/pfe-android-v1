package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 11/01/2017.
 */

public class ExtendedParcoursSubPartner implements Parcelable{

    private  long c_bpparcours_id;
    private  long c_bpparc_sub_id;
    private  long c_bpsub_id;
    private  long c_bpparc_visit_id;
    private  String name ;
    private  String processeing ;
    private  String gps ;

    public ExtendedParcoursSubPartner() {
    }


    protected ExtendedParcoursSubPartner(Parcel in) {
        c_bpparcours_id = in.readLong();
        c_bpparc_sub_id = in.readLong();
        c_bpsub_id = in.readLong();
        c_bpparc_visit_id = in.readLong();
        name = in.readString();
        processeing = in.readString();
        gps = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(c_bpparcours_id);
        dest.writeLong(c_bpparc_sub_id);
        dest.writeLong(c_bpsub_id);
        dest.writeLong(c_bpparc_visit_id);
        dest.writeString(name);
        dest.writeString(processeing);
        dest.writeString(gps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExtendedParcoursSubPartner> CREATOR = new Creator<ExtendedParcoursSubPartner>() {
        @Override
        public ExtendedParcoursSubPartner createFromParcel(Parcel in) {
            return new ExtendedParcoursSubPartner(in);
        }

        @Override
        public ExtendedParcoursSubPartner[] newArray(int size) {
            return new ExtendedParcoursSubPartner[size];
        }
    };

    public long getC_bpparcours_id() {
        return c_bpparcours_id;
    }

    public void setC_bpparcours_id(long c_bpparcours_id) {
        this.c_bpparcours_id = c_bpparcours_id;
    }

    public long getC_bpparc_sub_id() {
        return c_bpparc_sub_id;
    }

    public void setC_bpparc_sub_id(long c_bpparc_sub_id) {
        this.c_bpparc_sub_id = c_bpparc_sub_id;
    }

    public long getC_bpsub_id() {
        return c_bpsub_id;
    }

    public void setC_bpsub_id(long c_bpsub_id) {
        this.c_bpsub_id = c_bpsub_id;
    }

    public long getC_bpparc_visit_id() {
        return c_bpparc_visit_id;
    }

    public void setC_bpparc_visit_id(long c_bpparc_visit_id) {
        this.c_bpparc_visit_id = c_bpparc_visit_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcesseing() {
        return processeing;
    }

    public void setProcesseing(String processeing) {
        this.processeing = processeing;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

}
