package com.labrosse.suivicommercial.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SharedPrefsHelper {

	public static final String TAG = SharedPrefsHelper.class.getSimpleName();

	private static String mPrefName = "prefs";
	private static Context mContext;

	public static void init(Context context, String prefs) {

		mContext = context;
		mPrefName = prefs;
	}

	/*
	 * SHARED PREFERENCE
	 */

	private static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences(mPrefName, Context.MODE_PRIVATE);
	}

	/*
	 * CONTAINS
	 */
	public static boolean containsValue(Context context, String key) {
		return getPrefs(context).contains(key);
	}

	public static boolean containsValue(Context context, int key) {
		return getPrefs(context).contains(context.getString(key));
	}

	/*
	 * SAVE
	 */
	public static void saveBoolean(Context context, int resValue, boolean value) {

		getPrefs(context).edit().putBoolean(context.getString(resValue), value).commit();
	}

	public static void saveInt(Context context,int resValue, int value) {

		getPrefs(context).edit().putInt(context.getString(resValue), value).commit();
	}

	/*
	 * GET
	 */
	public static boolean getBoolean(Context context, int resValue, boolean defValue) {
		return getPrefs(context).getBoolean(context.getString(resValue), defValue);
	}

	public static int getInt(Context context, int resValue, int defValue) {
		return getPrefs(context).getInt(context.getString(resValue), defValue);
	}

	public static void setListPreferences(Context context, int resValue, String gps) {
		Set<String> newSet = new HashSet<String>();
		ArrayList<String> newList;
		if(getPrefs(context).contains(context.getString(resValue))){
			newList = retriveListPreferences(context, resValue);
		}else{
			newList = new ArrayList<>();
		}
		newList.add(gps);
		newSet.addAll(newList);

		getPrefs(context).edit().putStringSet(context.getString(resValue), newSet).commit();
	}

	public static ArrayList<String> retriveListPreferences(Context context, int resValue) {
		Set<String> set = getPrefs(context).getStringSet(context.getString(resValue), null);
		ArrayList<String> newList = new ArrayList<>();
		newList.addAll(set);
		return newList;
	}

}