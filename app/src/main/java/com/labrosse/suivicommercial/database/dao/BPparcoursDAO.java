package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.AdUser;
import com.labrosse.suivicommercial.model.database.BPparcours;
import com.labrosse.suivicommercial.model.database.ExtendedParcoursSubPartner;

import java.util.ArrayList;
import java.util.Calendar;


public class BPparcoursDAO extends BasicDAO {

    /**
     *  Singleton
     */

    private static BPparcoursDAO sInstance;

    public static synchronized BPparcoursDAO getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new BPparcoursDAO(context);
        }
        return sInstance;
    }

    public BPparcoursDAO(Context context) {
        super();
    }

    /**
     * Create new parcours
     */

    public long addBPparcours(BPparcours bPparcours) {
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_ID, bPparcours.getC_bpparcours_id());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID, bPparcours.getAd_user_id());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_STARTDATE, bPparcours.getStartDate());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_ENDDATE, bPparcours.getEndDate());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_PROCESSING, bPparcours.getProcessing());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_STAT, bPparcours.getStat());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_GPSSTARTREAL, bPparcours.getGpsStartReal());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_GPSSTARTTHE, bPparcours.getGpsStartTheoric());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_KMTHEPROPOSED, bPparcours.getKmTheoricProposed());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_KMTHEDO, bPparcours.getKmTheoricDone());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_KMREALDO, bPparcours.getKmRealDone());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_SYNCHRONISED, bPparcours.getSynchronised());

        return getWritableDatabase().insert(DatabaseHelper.TABLE_C_BPPARCOURS, null, cv);
    }

    public long addBPparcours(AdUser user) {

        SQLiteDatabase databse = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.putNull(EntryKey.C_BPPARCOURS_TABLE.KEY_ID);
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_VALUE, String.valueOf(Calendar.getInstance().getTimeInMillis()));
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID, user.getAd_user_id());
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_STARTDATE, getSynchroniseDate(Calendar.getInstance().getTime()));
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_PROCESSING, EntryKey.C_BPPARCOURS_TABLE.KEY_IS_KEY_PROCESSING);
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_STAT, EntryKey.C_BPPARCOURS_TABLE.KEY_IS_STARTED);
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_SYNCHRONISED, EntryKey.KEY_IS_NOT_SYNCHRONISED);

        return databse.insert(DatabaseHelper.TABLE_C_BPPARCOURS, null, cv);
    }

    /**
     * Select parcour
     */

    public ArrayList<BPparcours> getBPparcoursforUser(long ad_user_id) {

        String selection = EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(ad_user_id)};
        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPPARCOURS, null, selection, selectionArgs, null,
                        null, null);
        cursor.moveToFirst();

        ArrayList<BPparcours> pparcoursArrayList = new ArrayList<>();
        BPparcours bPparcours;

        while (cursor.moveToNext()) {
            bPparcours = getBPparcoursFromCursor(cursor);
            pparcoursArrayList.add(bPparcours);
        }

        return pparcoursArrayList;
    }

    public long getActiveBPparcoursforUser(AdUser user) {
        long bPparcours = -1;

        // Selection
        String selection =  EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? AND "
                            + EntryKey.C_BPPARCOURS_TABLE.KEY_PROCESSING + " = ?";

        // selectionArgs
        String[] selectionArgs = {String.valueOf(user.getAd_user_id()), EntryKey.C_BPPARCOURS_TABLE.KEY_IS_KEY_PROCESSING};

        // Get Database
        SQLiteDatabase databse = getWritableDatabase();

        // Query database
        Cursor cursor = databse.query(DatabaseHelper.TABLE_C_BPPARCOURS, null,
                        selection, selectionArgs, null, null, null);

        // Get parcours
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            bPparcours = cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_ID));
            cursor.close();
        }

        return bPparcours;
    }

    public BPparcours getParcours(long parcoursId) {
        BPparcours bPparcours = null;

        // Selection
        String selection =  EntryKey.C_BPPARCOURS_TABLE.KEY_ID + " = ? ";

        // selectionArgs
        String[] selectionArgs = {String.valueOf(parcoursId) };

        // Get Database
        SQLiteDatabase databse = getReadableDataBase();

        // Query database
        Cursor cursor = databse.query(DatabaseHelper.TABLE_C_BPPARCOURS, null,
                selection, selectionArgs, null, null, null);

        // Get parcours
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            bPparcours = getBPparcoursFromCursor(cursor);
            cursor.close();
        }

        return bPparcours;
    }

    public ExtendedParcoursSubPartner getActiveVisit(long parcoursId){
        StringBuilder sqlQuery = new StringBuilder(" SELECT c_bpparc_sub.C_BPPARCOURS_ID , ")
                .append (" c_bpsub.C_BPSUB_ID,")
                .append (" c_bpparc_sub.C_BPPARC_SUB_ID, ")
                .append (" c_bpsub.NAME, ")
                .append (" c_bpparc_visit.C_BPPARC_VISIT_ID, ")
                .append (" c_bpparc_visit.PROCESSING, ")
                .append (" C_BPSUB.GPS ")
                .append ("FROM C_BPPARC_SUB c_bpparc_sub ")
                .append(" INNER JOIN C_BPSUB c_bpsub on c_bpsub.C_BPSUB_ID = c_bpparc_sub.C_BPSUB_ID ")
                .append(" INNER JOIN C_BPPARC_VISIT  c_bpparc_visit on c_bpparc_visit.C_BPSUB_ID = c_bpsub.C_BPSUB_ID ")
                .append(" WHERE c_bpparc_sub.C_BPPARCOURS_ID = ? AND c_bpparc_visit.PROCESSING = ?" );

        SQLiteDatabase database = getReadableDataBase();

        // selectionArgs
        String[] selectionArgs = {String.valueOf(parcoursId), EntryKey.C_BPPARC_VISIT_TABLE.KEY_IS_KEY_PROCESSING };

        Cursor cursor = database.rawQuery(sqlQuery.toString(), selectionArgs);

        ExtendedParcoursSubPartner extendedParcoursSubPartner = null;
        // Get parcours
        if (cursor != null && cursor.getCount() > 0){

            cursor.moveToFirst();
            try {
                extendedParcoursSubPartner = getExtendedBPparcoursFromCursor(cursor);
            }catch (Exception exp){

            }
        }

        if(cursor != null){
            cursor.close();
        }

        return extendedParcoursSubPartner;
    }

    public ArrayList<ExtendedParcoursSubPartner> getExtendedSubPartners (long parcoursId){

        StringBuilder sqlQuery = new StringBuilder(" SELECT c_bpparc_sub.C_BPPARCOURS_ID , ")
                                .append ("c_bpsub.C_BPSUB_ID,")
                                .append ("c_bpparc_sub.C_BPPARC_SUB_ID, ")
                                .append ("c_bpsub.NAME, ")
                                .append (" -1 as C_BPPARC_VISIT_ID, ")
                                .append (" '' as Processing, ")
                                .append (" c_bpsub.GPS ")
                                .append ("FROM C_BPPARC_SUB c_bpparc_sub ")
                                .append(" INNER JOIN C_BPSUB c_bpsub on c_bpsub.C_BPSUB_ID = c_bpparc_sub.C_BPSUB_ID ")
                                .append(" WHERE c_bpparc_sub.C_BPPARCOURS_ID = ? " );

        SQLiteDatabase database = getReadableDataBase();

        // selectionArgs
        String[] selectionArgs = {String.valueOf(parcoursId) };

        Cursor cursor = database.rawQuery(sqlQuery.toString(), selectionArgs);

        ArrayList<ExtendedParcoursSubPartner> extendedParcoursSubPartnerArrayList = new ArrayList<>();

        ExtendedParcoursSubPartner extendedParcoursSubPartner;

        // Get parcours
        if (cursor != null && cursor.getCount() > 0){
            Log.e("getExtendedSubPartners", cursor.getColumnCount() + " " + cursor.getColumnCount());

           while (cursor.moveToNext()){
               try {
                   extendedParcoursSubPartner = getExtendedBPparcoursFromCursor(cursor);
                   extendedParcoursSubPartnerArrayList.add(extendedParcoursSubPartner);
               }catch (Exception exp){

               }
           }
        }

        if(cursor != null){
            cursor.close();
        }

        return extendedParcoursSubPartnerArrayList;
    }

    public ArrayList<ExtendedParcoursSubPartner> getProcessingVisit(long parcoursId){
        StringBuilder sqlQuery = new StringBuilder(" SELECT c_bpparc_visit.C_BPPARCOURS_ID , ")
                .append (" c_bpsub.C_BPSUB_ID,")
                .append (" - 1 AS C_BPPARC_SUB_ID, ")
                .append (" c_bpsub.NAME, ")
                .append (" c_bpparc_visit.C_BPPARC_VISIT_ID, ")
                .append (" c_bpparc_visit.PROCESSING, ")
                .append (" C_BPSUB.GPS ")
                .append ("FROM C_BPPARC_VISIT c_bpparc_visit ")
                .append(" INNER JOIN C_BPSUB c_bpsub on c_bpsub.C_BPSUB_ID = c_bpparc_visit.C_BPSUB_ID ")
                .append(" WHERE c_bpparc_visit.C_BPPARCOURS_ID = ? ORDER BY c_bpparc_visit.C_BPPARC_VISIT_ID DESC" );

        // selectionArgs
        String[] selectionArgs = {  String.valueOf(parcoursId) };

        Cursor cursor = getReadableDataBase().rawQuery(sqlQuery.toString(), selectionArgs);

        ArrayList<ExtendedParcoursSubPartner> extendedParcoursSubPartnerArrayList = new ArrayList<>();

        ExtendedParcoursSubPartner extendedParcoursSubPartner;

        // Get parcours
        if (cursor != null && cursor.getCount() > 0){
            Log.e("getExtendedSubPartners", cursor.getColumnCount() + " " + cursor.getColumnCount());

            while (cursor.moveToNext()){
                try {
                    extendedParcoursSubPartner = getExtendedBPparcoursFromCursor(cursor);
                    extendedParcoursSubPartnerArrayList.add(extendedParcoursSubPartner);
                }catch (Exception exp){

                }
            }
        }

        if(cursor != null){
            cursor.close();
        }

        return extendedParcoursSubPartnerArrayList;

    }

    private ExtendedParcoursSubPartner getExtendedBPparcoursFromCursor(Cursor cursor) {
        ExtendedParcoursSubPartner bPparcours = new ExtendedParcoursSubPartner();

        bPparcours.setC_bpparcours_id(cursor.getLong(0));
        bPparcours.setC_bpsub_id(cursor.getLong(1));
        bPparcours.setC_bpparc_sub_id(cursor.getLong(2));
        bPparcours.setName(cursor.getString(3));
        bPparcours.setC_bpparc_visit_id(cursor.getLong(4));
        bPparcours.setProcesseing(cursor.getString(5));
        bPparcours.setGps(cursor.getString(6));
        return  bPparcours;
    }

    /**
     *
     * Update parcours
     */

    public boolean updateParcoursStatus(long ad_user_id, long bPparcours_id, String status){
        ContentValues values = new ContentValues();
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_STAT, status);

        String selection = EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? AND " + EntryKey.C_BPPARCOURS_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(ad_user_id), String.valueOf(bPparcours_id) };

        SQLiteDatabase databse = getWritableDatabase();

        int count = databse.update(
                DatabaseHelper.TABLE_C_BPPARCOURS,
                values,
                selection,
                selectionArgs);

        return count > 0;
    }

    public boolean stopParcours(long ad_user_id, long c_bpparcours_id){
        ContentValues values = new ContentValues();

        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_ENDDATE, getSynchroniseDate(Calendar.getInstance().getTime()));
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_PROCESSING, EntryKey.C_BPPARCOURS_TABLE.KEY_IS_KEY_NOT_PROCESSING);
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_STAT, EntryKey.C_BPPARCOURS_TABLE.KEY_IS_STOPED);

        // KMH
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_KMREALDO, 0);
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_KMTHEPROPOSED, 0);

        String selection = EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? AND " + EntryKey.C_BPPARCOURS_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(ad_user_id), String.valueOf(c_bpparcours_id) };

        int count = getWritableDatabase().update(
                DatabaseHelper.TABLE_C_BPPARCOURS,
                values,
                selection,
                selectionArgs);

        return count > 0;

    }

    public boolean updateParcoursLocation(long ad_user_id, long c_bpparcours_id, String realLocation, String thoricLocation){
        ContentValues values = new ContentValues();

        // GPS
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_GPSSTARTREAL, realLocation);
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_GPSSTARTTHE, thoricLocation);

        String selection = EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? AND " + EntryKey.C_BPPARCOURS_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(ad_user_id), String.valueOf(c_bpparcours_id) };

        int count = getWritableDatabase().update(
                DatabaseHelper.TABLE_C_BPPARCOURS,
                values,
                selection,
                selectionArgs);

        return count > 0;

    }

    public boolean updateParcoursThericDistanceDone(long ad_user_id, long c_bpparcours_id,  int thoricdistance){
        ContentValues values = new ContentValues();

        // Distaance
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_KMTHEDO, thoricdistance);

        String selection = EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? AND " + EntryKey.C_BPPARCOURS_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(ad_user_id), String.valueOf(c_bpparcours_id) };

        int count = getWritableDatabase().update(
                DatabaseHelper.TABLE_C_BPPARCOURS,
                values,
                selection,
                selectionArgs);

        return count > 0;

    }
    /**
     * Common
     */

    private BPparcours getBPparcoursFromCursor(Cursor cursor) {
        BPparcours bPparcours = new BPparcours();

        // Ids
        bPparcours.setC_bpparcours_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_ID)));
        bPparcours.setValue(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_VALUE)));
        bPparcours.setAd_user_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID)));

        // Dates
        bPparcours.setStartDate(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_STARTDATE)));
        bPparcours.setEndDate(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_ENDDATE)));

        // Status
        bPparcours.setProcessing(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_PROCESSING)));
        bPparcours.setStat(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_STAT)));

        // Gps
        bPparcours.setGpsStartReal(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_GPSSTARTREAL)));
        bPparcours.setGpsStartTheoric(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_GPSSTARTTHE)));

        // Km
        bPparcours.setKmTheoricProposed(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_KMTHEPROPOSED)));
        bPparcours.setKmTheoricDone(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_KMTHEDO)));
        bPparcours.setKmRealDone(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_KMREALDO)));

        bPparcours.setSynchronised(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARCOURS_TABLE.KEY_SYNCHRONISED)));

        return bPparcours;
    }

    public boolean setParcourSychrnised(int ad_user_id, long c_bpparcours_id) {

        ContentValues values = new ContentValues();
        values.put(EntryKey.C_BPPARCOURS_TABLE.KEY_SYNCHRONISED, "Y");

        String selection = EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? AND " + EntryKey.C_BPPARCOURS_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(ad_user_id), String.valueOf(c_bpparcours_id) };

        int count = getWritableDatabase().update(
                DatabaseHelper.TABLE_C_BPPARCOURS,
                values,
                selection,
                selectionArgs);

        return count > 0;

    }

    public ArrayList<BPparcours> getParcoursToSynchronise(int ad_user_id) {

        String selection = EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? AND "
                            + EntryKey.C_BPPARCOURS_TABLE.KEY_PROCESSING + " = ? AND "
                            +  EntryKey.C_BPPARCOURS_TABLE.KEY_SYNCHRONISED + " = ?";
        String[] selectionArgs = {String.valueOf(ad_user_id), "N", "N"};

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPPARCOURS , null, selection, selectionArgs, null,
                        null, null);

        ArrayList<BPparcours> pparcoursArrayList = new ArrayList<>();
        BPparcours bPparcours;

        while (cursor.moveToNext()) {
            bPparcours = getBPparcoursFromCursor(cursor);
            pparcoursArrayList.add(bPparcours);
        }

        return pparcoursArrayList;
    }

    public boolean setNonSynchronised(){
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_SYNCHRONISED, "N");
        int count = getWritableDatabase().update(DatabaseHelper.TABLE_C_BPPARCOURS, cv, null, null);
        return count > 0;

    }


}
