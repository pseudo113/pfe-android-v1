package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.BPparcVisitePicture;

import java.util.ArrayList;

import static android.R.attr.id;

public class BPparcVisitePictureDAO extends BasicDAO {


    /**
     * Singleton
     */

    private static BPparcVisitePictureDAO sInstance;

    public static synchronized BPparcVisitePictureDAO getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BPparcVisitePictureDAO(context);
        }
        return sInstance;
    }

    public BPparcVisitePictureDAO(Context context) {
        super();
    }

    public long addBPparcVisitePicture(BPparcVisitePicture picture) {
        ContentValues cv = new ContentValues();
        cv.putNull(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_ID);
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_C_BPPARC_VISIT_ID, picture.getC_bpparc_visit_id());
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PIC, picture.getPicture());
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PICNAME, picture.getPictureName());
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PICDATE, picture.getPictureDate());
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_SYNCHRONISED, EntryKey.KEY_IS_NOT_SYNCHRONISED);
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_VISIT_VALUE, picture.getVisitValue());

        return getWritableDatabase().insert(DatabaseHelper.TABLE_C_BPPARC_VISIT_PIC, null, cv);
    }

    public BPparcVisitePicture getBPparcVisitePicture(long id) {

        String selection = EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = getReadableDataBase().query(DatabaseHelper.TABLE_C_BPPARC_VISIT_PIC, null,
                selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            BPparcVisitePicture bPparcVisitePicture = getBPparcVisitePictureFromCursor(cursor);
            cursor.close();
            return bPparcVisitePicture;
        }

        return null;
    }

    ArrayList<BPparcVisitePicture> getBPparcVisitePictureForVisite() {

        ArrayList<BPparcVisitePicture> pictures = new ArrayList<>();

        String selection = EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_C_BPPARC_VISIT_ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = getReadableDataBase().query(DatabaseHelper.TABLE_C_BPPARC_VISIT_PIC, null,
                selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                BPparcVisitePicture bPparcVisitePicture = getBPparcVisitePictureFromCursor(cursor);
                pictures.add(bPparcVisitePicture);

            }
            cursor.close();
        }

        return pictures;
    }

    private BPparcVisitePicture getBPparcVisitePictureFromCursor(Cursor cursor) {

        BPparcVisitePicture bPparcVisitePicture = new BPparcVisitePicture();

        bPparcVisitePicture.setC_visit_pic_id(cursor.getLong(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_ID)));

        bPparcVisitePicture.setC_bpparc_visit_id(cursor.getLong(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_C_BPPARC_VISIT_ID)));

        bPparcVisitePicture.setPicture(cursor.getBlob(cursor.getColumnIndex((EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PIC))));

        bPparcVisitePicture.setPictureName(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PICNAME)));

        bPparcVisitePicture.setPictureDate(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PICDATE)));

        bPparcVisitePicture.setSynchronised(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_SYNCHRONISED)));

        bPparcVisitePicture.setVisitValue(cursor.getString(cursor.getColumnIndex(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_VISIT_VALUE)));



        return bPparcVisitePicture;
    }

    public ArrayList<BPparcVisitePicture> getPictureToSychronise() {

        String selection = EntryKey.C_BPPARC_VISIT_TABLE.KEY_SYNCHRONISED + " = ?";
        String[] selectionArgs = {EntryKey.KEY_IS_NOT_SYNCHRONISED};


        Cursor cursor = getReadableDataBase().query(DatabaseHelper.TABLE_C_BPPARC_VISIT_PIC, null,
                selection, selectionArgs, null, null, null, "10");

        ArrayList<BPparcVisitePicture> pictures = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                BPparcVisitePicture bPparcVisitePicture = getBPparcVisitePictureFromCursor(cursor);
                pictures.add(bPparcVisitePicture);

            }
            cursor.close();
        }

        return pictures;
    }

    public int setSynchronised(long c_bpparc_visit_id, long c_visit_pic_id) {
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_SYNCHRONISED, "Y");

        String selection = EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_ID + " = ? AND " + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_C_BPPARC_VISIT_ID + " =  ?";

        String[] selectionArgs = { String.valueOf(c_visit_pic_id), String.valueOf(c_bpparc_visit_id) };

        return getWritableDatabase().update(DatabaseHelper.TABLE_C_BPPARC_VISIT_PIC, cv, selection, selectionArgs);
    }

    public boolean setNonSynchronised(){
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_SYNCHRONISED, "N");
        int count = getWritableDatabase().update(DatabaseHelper.TABLE_C_BPPARC_VISIT_PIC, cv, null, null);
        return count > 0;
    }

    public int updatePicture(BPparcVisitePicture picture) {

        ContentValues cv = new ContentValues();
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_C_BPPARC_VISIT_ID, picture.getC_bpparc_visit_id());
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PIC, picture.getPicture());
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PICNAME, picture.getPictureName());
        cv.put(EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_PICDATE, picture.getPictureDate());

        String selection = EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_ID + " = ? AND " + EntryKey.C_BPPARC_VISIT_PIC_TABLE.KEY_C_BPPARC_VISIT_ID + " =  ?";

        String[] selectionArgs = { String.valueOf(picture.getC_visit_pic_id()), String.valueOf(picture.getC_bpparc_visit_id()) };

        return getWritableDatabase().update(DatabaseHelper.TABLE_C_BPPARC_VISIT_PIC, cv, selection, selectionArgs);
    }
}


