package ru.igarin.bgtraining.ui;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.igarin.bgtraining.Alarms;
import ru.igarin.bgtraining.EX;
import ru.igarin.bgtraining.R;
import ru.igarin.bgtraining.db.DataReminder;

public class ReminderActivity extends ActionBarActivity {

    protected static final String INTENT_EXTRA_TYPE = "INTENT_EXTRA_TYPE";
    private EX mEX = EX.LINGVISTICHESKIE_PIRAMIDY;

    private CheckBox[] mCheckBoxes;
    private TextView[] mTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            onBaseRestoreInstanceState(savedInstanceState);
        } else if (intent != null) {
            onBaseRestoreInstanceState(intent);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.item_reminder);
        initView();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(mEX.string);
    }

    boolean canceled = false;

    private void onClicked(final int day) {
        canceled = false;

        DataReminder reminder = DataReminder.get(mEX, day);

        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (!canceled) {
                    mCheckBoxes[day].setChecked(true);
                    DataReminder reminder = DataReminder.get(mEX, day);
                    reminder.hourOfDay = hourOfDay;
                    reminder.minute = minute;
                    reminder.active = true;
                    reminder.update();
                    mTextViews[day].setText(getTime(reminder));

                }

            }
        }, reminder.hourOfDay, reminder.minute, true);

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            canceled = true;
                        }
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void initView() {
        mCheckBoxes = new CheckBox[7];
        mCheckBoxes[0] = (CheckBox) findViewById(R.id.check1);
        mCheckBoxes[1] = (CheckBox) findViewById(R.id.check2);
        mCheckBoxes[2] = (CheckBox) findViewById(R.id.check3);
        mCheckBoxes[3] = (CheckBox) findViewById(R.id.check4);
        mCheckBoxes[4] = (CheckBox) findViewById(R.id.check5);
        mCheckBoxes[5] = (CheckBox) findViewById(R.id.check6);
        mCheckBoxes[6] = (CheckBox) findViewById(R.id.check7);

        mTextViews = new TextView[7];
        mTextViews[0] = (TextView) findViewById(R.id.time1);
        mTextViews[1] = (TextView) findViewById(R.id.time2);
        mTextViews[2] = (TextView) findViewById(R.id.time3);
        mTextViews[3] = (TextView) findViewById(R.id.time4);
        mTextViews[4] = (TextView) findViewById(R.id.time5);
        mTextViews[5] = (TextView) findViewById(R.id.time6);
        mTextViews[6] = (TextView) findViewById(R.id.time7);

        for (int i = 0; i < 7; i++) {
            final int k = i;
            mCheckBoxes[k].setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
                    DataReminder reminder = DataReminder.get(mEX, k);
                    reminder.active = mCheckBoxes[k].isChecked();
                    reminder.update();
                }

            });
            DataReminder reminder = DataReminder.get(mEX, k);
            mCheckBoxes[k].setChecked(reminder.active);
            mTextViews[k].setText(getTime(reminder));

        }

        findViewById(R.id.view1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClicked(0);
            }

        });
        findViewById(R.id.view2).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClicked(1);
            }

        });
        findViewById(R.id.view3).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClicked(2);
            }

        });
        findViewById(R.id.view4).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClicked(3);
            }

        });
        findViewById(R.id.view5).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClicked(4);
            }

        });
        findViewById(R.id.view6).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClicked(5);
            }

        });
        findViewById(R.id.view7).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClicked(6);
            }

        });

        List<DataReminder> list = DataReminder.get(mEX);
        for (DataReminder d : list) {
            mCheckBoxes[d.day].setChecked(d.active);
            mTextViews[d.day].setText(getTime(d));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Alarms.scheduleRegularSync(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private String getTime(DataReminder reminder) {
        long time = DataReminder.triggerAtMillis(reminder);
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        return sdf.format(c.getTime());
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onBaseRestoreInstanceState(intent);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        onBaseSaveInstanceState(outState);
    }

    public void onBaseSaveInstanceState(final Bundle outState) {
        outState.putInt(INTENT_EXTRA_TYPE, mEX.ordinal());
    }

    ;

    public void onBaseRestoreInstanceState(final Bundle outState) {
        mEX = EX.values()[outState.getInt(INTENT_EXTRA_TYPE)];
    }

    ;

    public void onBaseRestoreInstanceState(final Intent intent) {
        mEX = EX.values()[intent.getIntExtra(INTENT_EXTRA_TYPE, mEX.ordinal())];
    }
}
