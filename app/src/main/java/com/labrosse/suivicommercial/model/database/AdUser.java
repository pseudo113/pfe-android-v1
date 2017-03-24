package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class AdUser implements Parcelable {

    private  int ad_user_id ;
    private  String name ;
    private  String email ;
    private  String password ;
    private  String phone ;
    private  String gps ;
    private  Date synchronisedate ;

    public AdUser() {
    }

    public AdUser(int ad_user_id, String name, String email, String password, String phone, String gps, Date synchronisedate) {
        this.ad_user_id = ad_user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gps = gps;
        this.synchronisedate = synchronisedate;
    }

    public int getAd_user_id() {
        return ad_user_id;
    }

    public void setAd_user_id(int ad_user_id) {
        this.ad_user_id = ad_user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public Date getSynchronisedate() {
        return synchronisedate;
    }

    public void setSynchronisedate(Date synchronisedate) {
        this.synchronisedate = synchronisedate;
    }

    protected AdUser(Parcel in) {
        ad_user_id = in.readInt();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        phone = in.readString();
        gps = in.readString();
        synchronisedate = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ad_user_id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(phone);
        dest.writeString(gps);

        if(synchronisedate != null){
            dest.writeLong(synchronisedate.getTime());
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdUser> CREATOR = new Creator<AdUser>() {
        @Override
        public AdUser createFromParcel(Parcel in) {
            return new AdUser(in);
        }

        @Override
        public AdUser[] newArray(int size) {
            return new AdUser[size];
        }
    };
}
