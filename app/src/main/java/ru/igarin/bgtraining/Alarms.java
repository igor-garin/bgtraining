package ru.igarin.bgtraining;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.List;

import ru.igarin.bgtraining.db.DataReminder;

public class Alarms {

    static final long DEFAULT_REGULAR_TIMEOUT_WEEK = 1000 * 60 * 60 * 24 * 7;

    public static long scheduleRegularSync(Context ctx) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        List<DataReminder> list = DataReminder.get();
        for (DataReminder reminder : list) {
            if (reminder.active) {
                am.setRepeating(AlarmManager.RTC_WAKEUP, DataReminder.triggerAtMillis(reminder),
                        DEFAULT_REGULAR_TIMEOUT_WEEK, createPendingIntent(ctx, reminder));
            }
        }
        return 0;
    }

    private static PendingIntent createPendingIntent(Context ctx, DataReminder reminder) {
        Intent intent = AppService.getIntent(ctx, reminder);
        Uri data1 = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data1);
        return PendingIntent.getService(ctx, reminder.hashCode(), intent, 0);
    }
}
