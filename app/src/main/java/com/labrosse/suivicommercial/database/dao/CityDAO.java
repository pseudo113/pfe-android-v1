package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.City;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ahmedhammami on 31/12/2016.
 */

public class CityDAO extends BasicDAO {

    /**
     * Singleton
     */

    private static CityDAO sInstance;

    public static CityDAO getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CityDAO(context);
        }
        return sInstance;
    }

    public CityDAO(Context context) {
        super();
    }

    /**
     * Create city
     */

    public long addCity(City city) {
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_CITY_TABLE.KEY_ID, city.getId());
        cv.put(EntryKey.C_CITY_TABLE.KEY_NAME, city.getName());
        cv.put(EntryKey.C_CITY_TABLE.KEY_SYNCHRONISED, getSynchroniseDate(Calendar.getInstance().getTime()));

        return getWritableDatabase().insert(DatabaseHelper.TABLE_C_CITY, null, cv);
    }

    /**
     * Select city
     */

    public boolean checkCityExists(City city){

        String selection =  EntryKey.C_CITY_TABLE.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(city.getId())};

        Cursor cursor =
                getWritableDatabase().query(DatabaseHelper.TABLE_C_CITY, null, selection, selectionArgs, null,
                        null, null);

        if(cursor != null && cursor.getCount() > 0){
            return true;
        }

        return false;
    }

    public ArrayList<City> getCities(){

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_CITY, null, null, null, null,
                        null, EntryKey.C_CITY_TABLE.KEY_NAME + " ASC");

        ArrayList<City> cityArrayList = new ArrayList<City>();
        City city;

        cursor.moveToFirst();
        while ( cursor.moveToNext()) {
            city = getCityFromCursor(cursor);
            cityArrayList.add(city);
        }
        cursor.close();

        return cityArrayList;

    }

    /**
     * Update city
     */

    public int updateCity(City city) {

        String selection =  EntryKey.C_CITY_TABLE.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(city.getId())};

        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_CITY_TABLE.KEY_NAME, city.getName());
        cv.put(EntryKey.C_CITY_TABLE.KEY_SYNCHRONISED, getSynchroniseDate(Calendar.getInstance().getTime()));

        return getWritableDatabase().update(DatabaseHelper.TABLE_C_CITY, cv, selection, selectionArgs);
    }

    /**
     * Common
     */
    private City getCityFromCursor(Cursor cursor) {
        City city = new City();

        city.setId(cursor.getInt(cursor.getColumnIndex(EntryKey.C_CITY_TABLE.KEY_ID)));
        city.setName(cursor.getString(cursor.getColumnIndex(EntryKey.C_CITY_TABLE.KEY_NAME)));
        city.setSynchronisedate(getSynchroniseDate(cursor.getString(cursor.getColumnIndex(EntryKey.C_CITY_TABLE.KEY_SYNCHRONISED))));

        return city;
    }


}
