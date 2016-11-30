package com.firstidea.android.brokerx.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
	
	public static void putSharedPreferencesInt(Context context, String key, int value){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit=preferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	
	public static void putSharedPreferencesBoolean(Context context, String key, boolean val){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit=preferences.edit();
		edit.putBoolean(key, val);
		edit.commit();
	}
	
	public static void putSharedPreferencesString(Context context, String key, String val){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit=preferences.edit();
		edit.putString(key, val);
		edit.commit();
	}
	
	public static void putSharedPreferencesFloat(Context context, String key, float val){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit=preferences.edit();
		edit.putFloat(key, val);
		edit.commit();
	}
	
	public static void putSharedPreferencesLong(Context context, String key, long val){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit=preferences.edit();
		edit.putLong(key, val);
		edit.commit();
	}
	
	public static long getSharedPreferencesLong(Context context, String key, long _default){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getLong(key, _default);
	}
	
	public static float getSharedPreferencesFloat(Context context, String key, float _default){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getFloat(key, _default);
	}
	
	public static String getSharedPreferencesString(Context context, String key, String _default){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(key, _default);
	}
	
	public static boolean isContains(Context context, String key){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.contains(key);
	}
	
	public static int getSharedPreferencesInt(Context context, String key, int _default){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(key, _default);
	}
	
	public static boolean getSharedPreferencesBoolean(Context context, String key, boolean _default){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(key, _default);
	}
	
	public static boolean containsKey(Context context, String key){
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.contains(key);
	}
	
	public static void remove(Context context, String key) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit=preferences.edit();
		edit.remove(key);
		edit.commit();
	}
	
	public static void clearAll(Context context) {
		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit=preferences.edit();
		edit.clear();
		edit.commit();
	}
}
