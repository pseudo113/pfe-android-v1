package com.labrosse.suivicommercial;

import android.app.Application;
import android.content.Context;

import com.labrosse.suivicommercial.database.DatabaseHelper;

/**
 * Created by ahmedhammami on 24/12/2016.
 */

public class MyApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        DatabaseHelper.getInstance(this).getWritableDatabase();
    }

    public static Context getContext(){
        return mContext;
    }

}
