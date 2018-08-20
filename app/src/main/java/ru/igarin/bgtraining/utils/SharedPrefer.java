package ru.igarin.bgtraining.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.igarin.bgtraining.App;

public class SharedPrefer {

    public static final String SP_WORD = "SP_WORD";
    public static final String SP_RUN = "SP_RUN";
    public static final String SP_RULES_FIRST = "SP_RULES_FIRST";

    protected static SharedPrefer instance;
    protected SharedPreferences prefs;

    public SharedPrefer() {
        App app = App.getInstance();
        prefs = PreferenceManager.getDefaultSharedPreferences(app);
    }

    public static SharedPrefer getInstance() {
        if (instance == null) {
            instance = new SharedPrefer();
        }
        return instance;
    }

    private void set(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void set(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void set(String key, long value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private void set(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean contains(String key) {
        return prefs.contains(key);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        set(key, value);
    }

    public void putInt(String key, int value) {
        set(key, value);
    }

    public void putBoolean(String key, boolean value) {
        set(key, value);
    }

    public long getLong(String key, long defValue) {
        return prefs.getLong(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        set(key, value);
    }

    public String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

}
