package ru.igarin.bgtraining;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class Const {

    public static final int MAX_REPEADT = 1000;
    public static final int MAX_ITER_COUNT = 1000;

    public static final String INFO_FILE_RULES = "info.html";
    public static final String INFO_FILE_ABOUT = "about.html";

    public static final String getFile(Context context, String fileName) {
        InputStream stream;
        int size;
        byte[] buffer = null;
        try {
            stream = context.getAssets().open(fileName);
            size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String str = new String(buffer);
        return str;
    }

}
