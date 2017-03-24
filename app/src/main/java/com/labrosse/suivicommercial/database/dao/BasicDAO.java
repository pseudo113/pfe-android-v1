package com.labrosse.suivicommercial.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.labrosse.suivicommercial.MyApplication;
import com.labrosse.suivicommercial.database.DatabaseHelper;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by ahmedhammami on 31/12/2016.
 */

public class BasicDAO {

    public final DateFormat mDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BasicDAO() {
    }

    public String getSynchroniseDate(Date date) {
        return mDateFormatter.format(date);
    }

    public String getSynchroniseDate(java.util.Date time) {
        return mDateFormatter.format(time);
    }

    public Date getSynchroniseDate(String date){

        if(date == null){
            return null;
        }

        java.util.Date newDate = null;
        try {
             newDate =  mDateFormatter.parse(date);

            if(newDate == null){
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date(newDate.getTime());
    }

    public int getNextSequence(String tableName, String columnName, String columnId, long id){

        int max = 0;
        String selection  =  String.format("SELECT max(%s) FROM %s WHERE %s = %d", columnName, tableName, columnId, id);
        SQLiteDatabase dataBase = getReadableDataBase();

        Cursor cursor = dataBase.rawQuery(selection, null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                //assing values
                max = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        cursor.close();

        return (max + 10);

    }

    /**
     * Clear table
     */

    public void clearTable(String tableName){
        getWritableDatabase().execSQL("DELETE FROM " + tableName);
    }

    public SQLiteDatabase getReadableDataBase(){
        return DatabaseHelper.getInstance(MyApplication.getContext()).getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return DatabaseHelper.getInstance(MyApplication.getContext()).getWritableDatabase();
    }


}
