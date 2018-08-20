package ru.igarin.bgtraining;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

import ru.igarin.bgtraining.db.DataReminder;
import ru.igarin.bgtraining.ui.ExersiseActivty;

public class AppService extends IntentService {

    private static final String INTENT_EXTRA_REMINDER_EX = "INTENT_EXTRA_REMINDER_EX";
    private static final String INTENT_EXTRA_REMINDER_DAY = "INTENT_EXTRA_REMINDER_DAY";

    public AppService() {
        super("AppService");
    }

    public static Intent getIntent(Context ctx, DataReminder reminder) {
        Intent ret = new Intent(ctx, AppService.class);
        ret.putExtra(INTENT_EXTRA_REMINDER_EX, reminder.ex);
        ret.putExtra(INTENT_EXTRA_REMINDER_DAY, reminder.day);
        return ret;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            EX ex = EX.valueOf(intent.getStringExtra(INTENT_EXTRA_REMINDER_EX));
            int day = intent.getIntExtra(INTENT_EXTRA_REMINDER_DAY, 0);
            DataReminder reminder = DataReminder.get(ex, day);
            if (reminder.active) {
                showNotification(ex);
            }
        }
    }

    private void showNotification(EX ex) {
        CharSequence text = getText(ex.string);
        Intent intent = new Intent(this, ExersiseActivty.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ExersiseActivty.INTENT_EXTRA_TYPE, ex.ordinal());
        Uri data1 = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data1);
        PendingIntent contentIntent = PendingIntent.getActivity(this, ex.id, intent, 0);

        int titleId = R.string.app_name;
        Notification noti = new NotificationCompat.Builder(this).setContentTitle(getText(titleId)).setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true).setContentIntent(contentIntent).build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ex.id, noti);

        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(this, defaultRingtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
