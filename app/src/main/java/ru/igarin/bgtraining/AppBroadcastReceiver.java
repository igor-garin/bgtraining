package ru.igarin.bgtraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.igarin.bgtraining.utils.L;

public class AppBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            L.d("ACTION_BOOT_COMPLETED");
            Alarms.scheduleRegularSync(context);
        }
    }

}
