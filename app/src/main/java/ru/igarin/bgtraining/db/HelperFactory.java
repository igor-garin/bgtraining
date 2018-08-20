package ru.igarin.bgtraining.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class HelperFactory {

    private static HelperDatabase databaseHelper;

    public static HelperDatabase getHelper() {
        return databaseHelper;
    }

    public static void setHelper(Context context) {
        databaseHelper = OpenHelperManager.getHelper(context,
                HelperDatabase.class);
    }

    public static void releaseHelper() {
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}