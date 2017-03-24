package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.BPparcSub;
import com.labrosse.suivicommercial.model.database.BPparcours;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ahmedhammami on 01/01/2017.
 */

public class BPparcSubDAO extends BasicDAO {

    /**
     * Singleton
     */

    private static BPparcSubDAO sInstance;

    public static synchronized BPparcSubDAO getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new BPparcSubDAO(context);
        }
        return sInstance;
    }

    public BPparcSubDAO(Context context) {
        super();
    }

    /**
     * Create new user
     */

    public long addBPparcSub(BPparcours C_bpparcours, long c_bpsub_id) {
        SQLiteDatabase database = getWritableDatabase();

        if(database.isOpen()){
            ContentValues cv = new ContentValues();
            cv.putNull(EntryKey.C_BPPARC_SUB_TABLE.KEY_ID);
            cv.put(EntryKey.C_BPPARC_SUB_TABLE.KEY_VALUE, String.valueOf(Calendar.getInstance().getTimeInMillis()));
            cv.put(EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPPARCOURS_ID, C_bpparcours.getC_bpparcours_id());
            cv.put(EntryKey.C_BPPARC_SUB_TABLE.KEY_BPPARCOURS_VALUE, C_bpparcours.getValue());
            cv.put(EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPSUB_ID, c_bpsub_id);
            cv.put(EntryKey.C_BPPARC_SUB_TABLE.KEY_SEQ, getNextSequence(C_bpparcours.getC_bpparcours_id()));
            cv.put(EntryKey.C_BPPARC_SUB_TABLE.KEY_SYNCHRONISED, "N");

            return database.insert(DatabaseHelper.C_BPPARC_SUB, null, cv);
        }

        return -1;
    }

    public ArrayList<BPparcSub> getBPparcSub(long C_bpparcours_id){

        String selection =  EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPPARCOURS_ID + " = ?";
        String[] selectionArgs = {String.valueOf(C_bpparcours_id)};

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.C_BPPARC_SUB, null, selection, selectionArgs, null,
                        null, null);

        ArrayList<BPparcSub> pParcSubsArrayList = new ArrayList<BPparcSub>();
        BPparcSub bPparcSub;

        while (cursor.moveToNext()) {
            bPparcSub = getBPparcSubFromCursor(cursor);
            pParcSubsArrayList.add(bPparcSub);
        }
        cursor.close();

        return pParcSubsArrayList;
    }

    private BPparcSub getBPparcSubFromCursor(Cursor cursor) {
        BPparcSub pparcSub = new BPparcSub();

        pparcSub.setC_bpparc_sub_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_SUB_TABLE.KEY_ID)));
        pparcSub.setValue(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_SUB_TABLE.KEY_VALUE)));
        pparcSub.setC_bpparcours_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPPARCOURS_ID)));
        pparcSub.setParcours_value(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_SUB_TABLE.KEY_BPPARCOURS_VALUE)));
        pparcSub.setC_bpsub_id(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPSUB_ID)));
        pparcSub.setSeq(cursor.getInt(cursor.getColumnIndex(EntryKey.C_BPPARC_SUB_TABLE.KEY_SEQ)));
        pparcSub.setSynchronised(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_SUB_TABLE.KEY_SYNCHRONISED)));

        return pparcSub;
    }

    private int getNextSequence(long c_bpparcours_id){
        return getNextSequence(DatabaseHelper.C_BPPARC_SUB, EntryKey.C_BPPARC_SUB_TABLE.KEY_SEQ, EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPPARCOURS_ID, c_bpparcours_id);
    }

    public boolean setParcSubSychronised(int c_bpparcours_id, int c_bpparc_sub_id) {

        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_SUB_TABLE.KEY_SYNCHRONISED, "Y");

        String selection =  EntryKey.C_BPPARC_SUB_TABLE.KEY_C_BPPARCOURS_ID + " = ? AND " + EntryKey.C_BPPARC_SUB_TABLE.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(c_bpparcours_id), String.valueOf(c_bpparc_sub_id)};

        int count = getWritableDatabase().update(DatabaseHelper.C_BPPARC_SUB, cv, selection, selectionArgs);

        return count > 0;

    }

    public ArrayList<BPparcSub> getBPparcSubTosSychronise(){

        String selection =  EntryKey.C_BPPARC_SUB_TABLE.KEY_SYNCHRONISED + " = ?";
        String[] selectionArgs = {EntryKey.KEY_IS_NOT_SYNCHRONISED};

        Cursor cursor =
                getReadableDataBase().query(DatabaseHelper.C_BPPARC_SUB, null,
                                    selection, selectionArgs,
                                    null, null, EntryKey.C_BPPARC_SUB_TABLE.KEY_SEQ + " ASC");

        ArrayList<BPparcSub> pParcSubsArrayList = new ArrayList<>();
        BPparcSub bPparcSub;

        while (cursor.moveToNext()) {
            bPparcSub = getBPparcSubFromCursor(cursor);
            pParcSubsArrayList.add(bPparcSub);
        }
        cursor.close();

        return pParcSubsArrayList;
    }

    public boolean setNonSynchronised(){
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_SUB_TABLE.KEY_SYNCHRONISED, "N");
        int count = getWritableDatabase().update(DatabaseHelper.C_BPPARC_SUB, cv, null, null);
        return count > 0;
    }

}
