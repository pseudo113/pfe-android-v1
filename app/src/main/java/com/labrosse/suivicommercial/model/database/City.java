package com.labrosse.suivicommercial.model.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;

/**
 * Created by ahmedhammami on 18/12/2016.
 */

public class City implements Parcelable {

    @SerializedName("c_city_id")
    @Expose
    int id;

    @SerializedName("name")
    @Expose
    String name;

    private Date synchronisedate ;
    private boolean isSelected;


    public City() {
    }

    public Date getSynchronisedate() {
        return synchronisedate;
    }

    public void setSynchronisedate(Date synchronisedate) {
        this.synchronisedate = synchronisedate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected City(Parcel in) {
        id = in.readInt();
        name = in.readString();
        synchronisedate.setTime(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(synchronisedate.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
