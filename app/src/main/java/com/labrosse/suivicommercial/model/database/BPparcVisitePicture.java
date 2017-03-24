package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by ahmedhammami on 18/12/2016.
 */

public class BPparcVisitePicture implements Parcelable {

    @SerializedName("c_bpparc_visit_pic_id")
    @Expose
    protected  long c_visit_pic_id;

    @SerializedName("c_bpparc_visit_id")
    @Expose
    protected  long c_bpparc_visit_id;

    @SerializedName("pic")
    @Expose
    protected  byte[] picture;

    @SerializedName("picname")
    @Expose
    protected  String pictureName;

    @SerializedName("picdate")
    @Expose
    protected  String pictureDate;

    @SerializedName("vvalue")
    @Expose
    protected String visitValue;

    protected  transient String synchronised;
    

    public BPparcVisitePicture() {
    }
    public long getC_visit_pic_id() {
        return c_visit_pic_id;
    }

    public void setC_visit_pic_id(long c_visit_pic_id) {
        this.c_visit_pic_id = c_visit_pic_id;
    }

    public long getC_bpparc_visit_id() {
        return c_bpparc_visit_id;
    }

    public void setC_bpparc_visit_id(long c_bpparc_visit_id) {
        this.c_bpparc_visit_id = c_bpparc_visit_id;
    }

    public  byte[] getPicture() {
        return picture;
    }

    public void setPicture( byte[] picture) {
        this.picture = picture;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureDate() {
        return pictureDate;
    }

    public void setPictureDate(String pictureDate) {
        this.pictureDate = pictureDate;
    }

    public String getSynchronised() {
        return synchronised;
    }

    public void setSynchronised(String synchronised) {
        this.synchronised = synchronised;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.c_visit_pic_id);
        dest.writeLong(this.c_bpparc_visit_id);
        dest.writeByteArray(this.picture);
        dest.writeString(this.pictureName);
        dest.writeString(this.pictureDate);
        dest.writeString(this.synchronised);
        dest.writeString(this.visitValue);
    }

    protected BPparcVisitePicture(Parcel in) {
        this.c_visit_pic_id = in.readLong();
        this.c_bpparc_visit_id = in.readLong();
        this.picture = in.createByteArray();
        this.pictureName = in.readString();
        this.pictureDate = in.readString();
        this.synchronised = in.readString();
        this.visitValue = in.readString();
    }

    public static final Creator<BPparcVisitePicture> CREATOR = new Creator<BPparcVisitePicture>() {
        @Override
        public BPparcVisitePicture createFromParcel(Parcel source) {
            return new BPparcVisitePicture(source);
        }

        @Override
        public BPparcVisitePicture[] newArray(int size) {
            return new BPparcVisitePicture[size];
        }
    };

    public void setVisitValue(String visitValue) {
        this.visitValue = visitValue;
    }

    public String getVisitValue() {
        return visitValue;
    }
}
