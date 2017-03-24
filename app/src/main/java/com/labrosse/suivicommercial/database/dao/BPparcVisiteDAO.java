package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.BPparcSub;
import com.labrosse.suivicommercial.model.database.BPparcVisite;
import com.labrosse.suivicommercial.model.database.BPparcours;

import java.util.ArrayList;
import java.util.Calendar;

import static com.labrosse.suivicommercial.database.DatabaseHelper.TABLE_C_BPPARC_VISIT;

public class BPparcVisiteDAO extends BasicDAO {


    /**
     * Singleton
     */

    private static BPparcVisiteDAO sInstance;

    public static synchronized BPparcVisiteDAO getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BPparcVisiteDAO(context);
        }
        return sInstance;
    }

    public BPparcVisiteDAO(Context context) {
        super();
    }

    public long addBPparcVisite(BPparcSub bPparcSub) {
        ContentValues cv = new ContentValues();
        cv.putNull(EntryKey.C_BPPARC_VISIT_TABLE.KEY_ID);
        cv.put(EntryKey.C_BPPARCOURS_TABLE.KEY_VALUE, String.valueOf(Calendar.getInstance().getTimeInMillis()));
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID, bPparcSub.getC_bpparcours_id());
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPSUB_ID, bPparcSub.getC_bpsub_id());
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_SEQ, getNextSequence(bPparcSub.getC_bpparcours_id()));
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_STARTDATE, getSynchroniseDate(Calendar.getInstance().getTime()));
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_PROCESSING, "Y");
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED, "N");

        return getWritableDatabase().insert(DatabaseHelper.TABLE_C_BPPARC_VISIT, null, cv);
    }

    public long addBPparcVisite(BPparcours c_bpparcours, long C_BPSUB_ID, String gps ){
        ContentValues cv = new ContentValues();
        cv.putNull(EntryKey.C_BPPARC_VISIT_TABLE.KEY_ID);
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_VALUE, String.valueOf(Calendar.getInstance().getTimeInMillis()));
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID, c_bpparcours.getC_bpparcours_id());
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_BPPARCOURS_VALUE, c_bpparcours.getValue());
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPSUB_ID, C_BPSUB_ID);
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_STARTDATE, getSynchroniseDate(Calendar.getInstance().getTime()));
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_SEQ, getNextSequence(c_bpparcours.getC_bpparcours_id()));
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_PROCESSING, EntryKey.C_BPPARC_VISIT_TABLE.KEY_IS_KEY_PROCESSING);
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_GPS, gps);
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED, EntryKey.KEY_IS_NOT_SYNCHRONISED);

        return getWritableDatabase().insert(DatabaseHelper.TABLE_C_BPPARC_VISIT, null, cv);
    }

    private BPparcVisite getBPparcVisiteFromCursor(Cursor cursor) {
        BPparcVisite bPparcVisite = new BPparcVisite();

        bPparcVisite.setC_bpparc_visit_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_ID)));
        bPparcVisite.setValue(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_VALUE)));
        bPparcVisite.setC_bpparcours_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID)));
        bPparcVisite.setParcours_value(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_BPPARCOURS_VALUE)));

        bPparcVisite.setC_bpsub_id(cursor.getInt(cursor.getColumnIndex((EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPSUB_ID))));
        bPparcVisite.setSeq(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_SEQ)));
        bPparcVisite.setStartDate(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_STARTDATE)));
        bPparcVisite.setEndDate(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_ENDDATE)));
        bPparcVisite.setGps(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_GPS)));
        bPparcVisite.setProcessing(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_PROCESSING)));
        bPparcVisite.setSynchronised(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED)));

        return bPparcVisite;
    }

    public BPparcVisite getBPparcVisite(long id){
        ContentValues values = new ContentValues();

        String selection = EntryKey.C_BPPARC_VISIT_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor =
                getReadableDataBase().query(TABLE_C_BPPARC_VISIT, null, selection, selectionArgs, null,
                        null, null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            BPparcVisite bPparcVisite = getBPparcVisiteFromCursor(cursor);
            cursor.close();
            return bPparcVisite;
        }

        return null;
    }

    public boolean stopActiveVisits(long c_bpparcours_id){
        ContentValues values = new ContentValues();
        values.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_ENDDATE, getSynchroniseDate(Calendar.getInstance().getTime()));
        values.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_PROCESSING, EntryKey.C_BPPARC_VISIT_TABLE.KEY_IS_KEY_NOT_PROCESSING);

        String selection = EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(c_bpparcours_id) };

        return getWritableDatabase().update(TABLE_C_BPPARC_VISIT, values, selection,  selectionArgs) > 0 ;
    }

    public boolean stopVisite(long id){
        ContentValues values = new ContentValues();
        values.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_ENDDATE, getSynchroniseDate(Calendar.getInstance().getTime()));
        values.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_PROCESSING, "N");

        String selection = EntryKey.C_BPPARC_VISIT_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(id) };

        return getWritableDatabase().update(TABLE_C_BPPARC_VISIT, values, selection,  selectionArgs) > 0 ;
    }

    private int getNextSequence(long C_BPPARCOURS_ID){
        return getNextSequence(DatabaseHelper.TABLE_C_BPPARC_VISIT, EntryKey.C_BPPARC_VISIT_TABLE.KEY_SEQ, EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID, C_BPPARCOURS_ID);
    }

    public boolean setVisiteSynchronsied(int c_bpparcours_id, int c_bpparc_visit_id) {
        ContentValues values = new ContentValues();
        values.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED, "Y");

        String selection = EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID + " = ? " +
                            "AND " + EntryKey.C_BPPARC_VISIT_TABLE.KEY_ID + " = ? ";

        String[] selectionArgs = { String.valueOf(c_bpparcours_id), String.valueOf(c_bpparc_visit_id) };

        return getWritableDatabase()
                .update(TABLE_C_BPPARC_VISIT,
                        values,
                        selection,
                        selectionArgs) > 0 ;

    }

    public ArrayList<BPparcVisite> getVisitsToSynchronise() {

        // TODO Add join with aduser id
        String selection =  EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED + " = ? AND "
                            + EntryKey.C_BPPARC_VISIT_TABLE.KEY_PROCESSING + " = ? ";
        String[] selectionArgs = {EntryKey.KEY_IS_NOT_SYNCHRONISED, EntryKey.C_BPPARC_VISIT_TABLE.KEY_IS_KEY_NOT_PROCESSING};

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPPARC_VISIT, null,
                        selection, selectionArgs,
                        null, null, EntryKey.C_BPPARC_VISIT_TABLE.KEY_SEQ + " ASC");

        ArrayList<BPparcVisite> bPparcVisites = new ArrayList<>();
        BPparcVisite bPparcVisite;

        while (cursor.moveToNext()) {
            bPparcVisite = getBPparcVisiteFromCursor(cursor);
            bPparcVisites.add(bPparcVisite);
        }
        cursor.close();

        return bPparcVisites;
    }

    public String getVisitsForParcours(long ad_user_id) {

        StringBuilder sql = new StringBuilder(" SELECT ");
        sql.append("visit." + EntryKey.C_BPPARC_VISIT_TABLE.KEY_GPS);

        sql.append(" FROM " + DatabaseHelper.TABLE_C_BPPARC_VISIT + " visit ");
        sql.append(" INNER JOIN  " + DatabaseHelper.TABLE_C_BPPARCOURS + " parcours ");
        sql.append(" ON parcours." + EntryKey.C_BPPARCOURS_TABLE.KEY_ID + " = visit." + EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID);
        sql.append(" WHERE parcours." + EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ? ");
        sql.append(" AND visit." + EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED + " = ?");

        String[] selectionArgs = {String.valueOf(ad_user_id), "N"};

        Cursor cursor =
                getReadableDataBase().rawQuery(sql.toString(), selectionArgs);

        StringBuilder gpsVisites = new StringBuilder("");
        int i =  0;
        while (cursor.moveToNext()) {

            String gps = cursor.getString(0);
            if( i == 0 ){
                gpsVisites.append(gps);
            }
            else {
                gpsVisites.append("|").append(gps);
            }
            i++;
        }

        cursor.close();

        return gpsVisites.toString();
    }

    public BPparcVisite getActiveVisit(long C_BPPARCOURS_ID){
        String selection = EntryKey.C_BPPARC_VISIT_TABLE.KEY_C_BPPARCOURS_ID + " = ? AND "
                + EntryKey.C_BPPARC_VISIT_TABLE.KEY_PROCESSING + " = ?";

        String[] selectionArgs = {String.valueOf(C_BPPARCOURS_ID), EntryKey.C_BPPARC_VISIT_TABLE.KEY_IS_KEY_PROCESSING};

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.TABLE_C_BPPARC_VISIT, null,
                        selection, selectionArgs,
                        null, null, null);

        BPparcVisite bPparcVisite = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            bPparcVisite = getBPparcVisiteFromCursor(cursor);
        }
        cursor.close();

        return bPparcVisite;
    }

    public boolean setNonSynchronised(){
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED, "N");
        int count = getWritableDatabase().update(DatabaseHelper.TABLE_C_BPPARC_VISIT, cv, null, null);
        return count > 0;
    }
}
