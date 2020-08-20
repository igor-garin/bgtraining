package ru.igarin.bgtraining;

import android.app.Application;

import ru.igarin.bgtraining.db.HelperFactory;
import ru.igarin.bgtraining.utils.SharedPrefer;

public class App extends Application {

    protected static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }

}
