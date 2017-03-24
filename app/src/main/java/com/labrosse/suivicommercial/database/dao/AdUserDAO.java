package com.labrosse.suivicommercial.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.labrosse.suivicommercial.database.EntryKey;
import com.labrosse.suivicommercial.model.database.AdUser;

import java.util.ArrayList;
import java.util.Calendar;

import static com.labrosse.suivicommercial.database.DatabaseHelper.TABLE_AD_USER;

public class AdUserDAO  extends BasicDAO {

    private static final String TAG = "AdUserDAO";
    /**
     * Singleton
     */
    private static AdUserDAO sInstance;

    public static synchronized AdUserDAO getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AdUserDAO(context);
        }
        return sInstance;
    }

    public AdUserDAO(Context context) {
        super();
    }

    /**
     * Create new user
     */

    public long addUser(AdUser user) {
        ContentValues cv = new ContentValues();
        cv.put(EntryKey.AD_USER_TABLE.KEY_ID, user.getAd_user_id());
        cv.put(EntryKey.AD_USER_TABLE.KEY_NAME, user.getName());
        cv.put(EntryKey.AD_USER_TABLE.KEY_EMAIL, user.getEmail());
        cv.put(EntryKey.AD_USER_TABLE.KEY_PASSWORD, user.getPassword());
        cv.put(EntryKey.AD_USER_TABLE.KEY_PHONE, user.getPhone());
        cv.put(EntryKey.AD_USER_TABLE.KEY_GPS, user.getGps());

        String date = getSynchroniseDate(Calendar.getInstance().getTime());
        cv.put(EntryKey.AD_USER_TABLE.KEY_SYNCHRONISED, date);

        return getWritableDatabase().insert(TABLE_AD_USER, null, cv);
    }

    /**
     * Update user
     */

    public boolean setUserActive(AdUser user) {
        desactivateActiveUser();
        ContentValues values = new ContentValues();
        values.put(EntryKey.AD_USER_TABLE.KEY_ACTIVE, EntryKey.AD_USER_TABLE.KEY_AD_USER_IS_ACTIVE);

        String selection = EntryKey.AD_USER_TABLE.KEY_ID + " = ? ";
        String[] selectionArgs = { String.valueOf(user.getAd_user_id()) };

        SQLiteDatabase database = getWritableDatabase();

        int count = database.update(
                TABLE_AD_USER,
                values,
                selection,
                selectionArgs);

        return count > 0;
    }
    public boolean desactivateActiveUser() {
        ContentValues values = new ContentValues();
        values.put(EntryKey.AD_USER_TABLE.KEY_ACTIVE, EntryKey.AD_USER_TABLE.KEY_AD_USER_IS_INACTIVE);

        SQLiteDatabase database = getWritableDatabase();

        int count = database.update(
                TABLE_AD_USER,
                values,
                null,
                null);

        return count > 0;
    }

    public int desactivateAllUsers() {
        ContentValues values = new ContentValues();
        values.put(EntryKey.AD_USER_TABLE.KEY_ACTIVE, EntryKey.AD_USER_TABLE.KEY_AD_USER_IS_INACTIVE);

        int count = getWritableDatabase().update(
                TABLE_AD_USER,
                values,
                null,
                null);

        return count;
    }

    public int updateUser(AdUser user){
        ContentValues cv = new ContentValues();

        cv.put(EntryKey.AD_USER_TABLE.KEY_NAME, user.getName());
        cv.put(EntryKey.AD_USER_TABLE.KEY_EMAIL, user.getEmail());
        cv.put(EntryKey.AD_USER_TABLE.KEY_PASSWORD, user.getPassword());
        cv.put(EntryKey.AD_USER_TABLE.KEY_PHONE, user.getPhone());
        cv.put(EntryKey.AD_USER_TABLE.KEY_GPS, user.getGps());

        String date = getSynchroniseDate(Calendar.getInstance().getTime());
        cv.put(EntryKey.AD_USER_TABLE.KEY_SYNCHRONISED, date);

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        int count = 0;

        try {
            String selection =  EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ?";
            String[] selectionArgs = {String.valueOf(user.getAd_user_id())};

            count = db.update(TABLE_AD_USER, cv, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }

        return count;
    }

    /**
     * Select users
     * @return
     */

    public AdUser getUser(String user_name, String user_password){

        String selection = EntryKey.AD_USER_TABLE.KEY_NAME + " = ? AND "
                            + EntryKey.AD_USER_TABLE.KEY_PASSWORD + " = ?";
        String[] selectionArgs = { user_name, user_password};

        AdUser user = userFromCursor(selection, selectionArgs);

        return user;
    }

    public AdUser getActiveUser(){
        String selection = EntryKey.AD_USER_TABLE.KEY_ACTIVE + " = ? ";
        String[] selectionArgs = {String.valueOf(EntryKey.AD_USER_TABLE.KEY_AD_USER_IS_ACTIVE) };

        AdUser user = userFromCursor(selection, selectionArgs);

        return user;
    }

    public ArrayList<AdUser> getUsers(){
        AdUser user;

        Cursor cursor =
                getWritableDatabase().query(TABLE_AD_USER, null, null, null, null,
                        null, null);
        cursor.moveToFirst();

        ArrayList<AdUser> users = new ArrayList<>();

        while (cursor.moveToNext()) {
            user = new AdUser();
            user.setAd_user_id(cursor.getInt(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_PASSWORD)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_PHONE)));
            user.setGps(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_GPS)));
            users.add(user);
        }

        cursor.close();
        return users;
    }

    public boolean checkUserExists(AdUser user){
        String selection =  EntryKey.C_BPPARCOURS_TABLE.KEY_AD_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(user.getAd_user_id())};

        Cursor cursor =
                getReadableDataBase().query(TABLE_AD_USER, null, selection, selectionArgs, null,
                        null, null);

        if(cursor != null && cursor.getCount() > 0){
            return true;
        }

        return false;
    }

    /**
     * Common
     */

    @NonNull
    private AdUser userFromCursor(String selection, String[] selectionArgs) {
        AdUser user = null;

        SQLiteDatabase dataBase = getReadableDataBase();

        Cursor cursor =
                dataBase.query(TABLE_AD_USER, null, selection, selectionArgs, null,
                        null, null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            user = new AdUser();
            user.setAd_user_id(cursor.getInt(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_PASSWORD)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_PHONE)));
            user.setGps(cursor.getString(cursor.getColumnIndex(EntryKey.AD_USER_TABLE.KEY_GPS)));
            cursor.close();
        }

        return user;
    }

}
