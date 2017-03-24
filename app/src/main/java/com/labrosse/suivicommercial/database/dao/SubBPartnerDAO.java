package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.SubBPartner;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ahmedhammami on 31/12/2016.
 */

public class SubBPartnerDAO extends BasicDAO {

    /**
     * Singleton
     */

    private static SubBPartnerDAO sInstance;

    public static SubBPartnerDAO getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SubBPartnerDAO(context);
        }
        return sInstance;
    }

    public SubBPartnerDAO(Context context) {
        super();
    }

    /**
     * Create new subBPartner
     */

    public long addSubBPartner(SubBPartner subBPartner) {
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_ID, subBPartner.getC_bpsub_id());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_VALUE, subBPartner.getValue());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_NAME, subBPartner.getName());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_C_CITY_ID, subBPartner.getC_city_id());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_ADRESSE, subBPartner.getAdresse());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_GPS, subBPartner.getGps());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_SYNCHRONISEDATE, getSynchroniseDate(Calendar.getInstance().getTime()));

        return getWritableDatabase().insert(DatabaseHelper.TABLE_C_BPSUB, null, cv);
    }

    /**
     * Select subBPartner
     */

    public ArrayList<SubBPartner> getSubBPartnerByCity(long c_city_id){

        String selection =  EntryKey.C_BPSUB_TABLE.KEY_C_CITY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(c_city_id)};

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPSUB, null, selection, selectionArgs, null,
                        null, null);

        return createSubBPartnersList(cursor);
    }

    public ArrayList<SubBPartner> getAllSubBPartner(){
        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPSUB, null, null, null, null,
                        null, null);

        return createSubBPartnersList(cursor);
    }

    public boolean checkSubBPartnerExists(SubBPartner subBPartner){
        String selection =  EntryKey.C_BPSUB_TABLE.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(subBPartner.getC_bpsub_id())};

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPSUB, null, selection, selectionArgs, null,
                        null, null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        return false;
    }

    public SubBPartner getSubBPartner(int c_bpsub_id){

        String selection = EntryKey.C_BPSUB_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(c_bpsub_id) };

        Cursor cursor =  getReadableDataBase().query(DatabaseHelper.TABLE_C_BPSUB, null,
                        selection, selectionArgs, null, null, null);

        if(cursor != null && cursor.getCount() > 0 ){
            cursor.moveToFirst();

            SubBPartner subBPartner =  createSubBPartnerFromCursor(cursor);
            cursor.close();

            return subBPartner;
        }

        return null;
    }

    /**
     * Update subBPartner
     */

    public int updateSubBPartner(SubBPartner subBPartner){

        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_VALUE, subBPartner.getValue());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_NAME, subBPartner.getName());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_C_CITY_ID, subBPartner.getC_city_id());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_ADRESSE, subBPartner.getAdresse());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_GPS, subBPartner.getGps());
        cv.put(EntryKey.C_BPSUB_TABLE.KEY_SYNCHRONISEDATE, getSynchroniseDate(Calendar.getInstance().getTime()));

        String selection =  EntryKey.C_BPSUB_TABLE.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(subBPartner.getC_bpsub_id())};

        return getWritableDatabase().update(DatabaseHelper.TABLE_C_BPSUB, cv, selection, selectionArgs);
    }

    /**
     * Common
     */

    @NonNull
    private ArrayList<SubBPartner> createSubBPartnersList(Cursor cursor) {
        ArrayList<SubBPartner> subBPartnerArrayList = new ArrayList<>();
        SubBPartner subBPartner;
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                subBPartner = createSubBPartnerFromCursor(cursor);
                subBPartnerArrayList.add(subBPartner);
            }
        }

        return subBPartnerArrayList;
    }

    private SubBPartner createSubBPartnerFromCursor(Cursor cursor) {
        SubBPartner subBPartner = new SubBPartner();

        subBPartner.setC_bpsub_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPSUB_TABLE.KEY_ID)));
        subBPartner.setValue(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_TABLE.KEY_VALUE)));
        subBPartner.setName(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_TABLE.KEY_NAME)));
        subBPartner.setC_city_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPSUB_TABLE.KEY_C_CITY_ID)));
        subBPartner.setAdresse(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_TABLE.KEY_ADRESSE)));
        subBPartner.setGps(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_TABLE.KEY_GPS)));
        subBPartner.setSynchroniseDate(getSynchroniseDate(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPSUB_TABLE.KEY_SYNCHRONISEDATE))));

        return subBPartner;
    }

}
