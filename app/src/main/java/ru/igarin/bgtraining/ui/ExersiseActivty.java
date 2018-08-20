package ru.igarin.bgtraining.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import ru.igarin.bgtraining.EX;
import ru.igarin.bgtraining.R;
import ru.igarin.bgtraining.RandWordController;
import ru.igarin.bgtraining.StaticInfo;
import ru.igarin.bgtraining.db.DataRecord;
import ru.igarin.bgtraining.utils.SharedPrefer;


public class ExersiseActivty extends ActionBarActivity implements OnClickListener {

    public static final String INTENT_EXTRA_TYPE = "INTENT_EXTRA_TYPE";

    private EX mEX = EX.LINGVISTICHESKIE_PIRAMIDY;

    private Chronometer mChronometerMain;
    private Chronometer mChronometerWord;
    private TextView mWord;
    private RanDirView mRanDirView;
    private long timeWhenMainPause = 0;
    private long timeWhenWordPause = 0;
    private long activityId = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exersise);

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            onBaseRestoreInstanceState(savedInstanceState);
        } else if (intent != null) {
            onBaseRestoreInstanceState(intent);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RandWordController.getNextWord(this, mEX);

        mChronometerMain = (Chronometer) this.findViewById(R.id.chronometer_main);
        mChronometerWord = (Chronometer) this.findViewById(R.id.chronometer_word);
        mWord = (TextView) this.findViewById(R.id.word);
        mRanDirView = (RanDirView) findViewById(R.id.ranDirView1);

        if (SharedPrefer.getInstance().getBoolean(SharedPrefer.SP_RULES_FIRST + mEX.id, true)) {
            showRules(true);
            SharedPrefer.getInstance().putBoolean(SharedPrefer.SP_RULES_FIRST + mEX.id, false);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onBaseRestoreInstanceState(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText(mEX.string);

        mWord.setText(SharedPrefer.getInstance().getString(SharedPrefer.SP_WORD, ""));

        Button button = (Button) findViewById(R.id.button_next);
        button.setOnClickListener(this);
        button.setText(mEX.button_text);
        mChronometerMain.setBase(SystemClock.elapsedRealtime() + timeWhenMainPause);
        mChronometerMain.start();
        mChronometerWord.setBase(SystemClock.elapsedRealtime() + timeWhenWordPause);
        mChronometerWord.start();

        if (!mEX.equals(EX.LINGVISTICHESKIE_PIRAMIDY)) {
            if (this.getResources().getBoolean(R.bool.vizible_dir)) {
                mRanDirView.setVisibility(View.INVISIBLE);
            } else {
                mRanDirView.setVisibility(View.GONE);
            }

        } else {
            mRanDirView.setOnClickListener(this);
        }
        if (mEX.equals(EX.RESHENIE_PROBLEM)) {
            mWord.setTextSize(20);
            if (!this.getResources().getBoolean(R.bool.vizible_dir)) {
                mRanDirView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeWhenMainPause = mChronometerMain.getBase() - SystemClock.elapsedRealtime();
        mChronometerMain.stop();
        timeWhenWordPause = mChronometerWord.getBase() - SystemClock.elapsedRealtime();
        mChronometerWord.stop();

        DataRecord record = DataRecord.get(mEX, activityId);
        record.milliseconds = timeWhenMainPause * (-1);
        record.update();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next:
                mChronometerWord.setBase(SystemClock.elapsedRealtime());
                timeWhenWordPause = 0;
                RandWordController.getNextWord(this, mEX);
                mWord.setText(SharedPrefer.getInstance().getString(SharedPrefer.SP_WORD, ""));
                mRanDirView.changeDir();
                break;
            case R.id.ranDirView1:
                mRanDirView.changeDir();
                break;
        }

    }

    ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ex_main, menu);
        return true;
    }

    private void showRules(boolean showButton) {
        Intent intent = new Intent(this, InfoExActivity.class);
        StaticInfo si = new StaticInfo(mEX.rule_file, R.string.rules);
        si.ex = mEX.name();
        intent.putExtra(InfoExActivity.INTENT_EXTRA_INFO, si);
        intent.putExtra(InfoExActivity.INTENT_EXTRA_SHOW_BUTTON, showButton);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.rules: {
                showRules(false);
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
