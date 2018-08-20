package ru.igarin.bgtraining.ui;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import ru.igarin.bgtraining.EX;
import ru.igarin.bgtraining.R;
import ru.igarin.bgtraining.db.DataHistory;
import ru.igarin.bgtraining.utils.Utils;

public class StatActivity extends ActionBarActivity {

    protected static final String INTENT_EXTRA_TYPE = "INTENT_EXTRA_TYPE";
    private EX mEX = EX.LINGVISTICHESKIE_PIRAMIDY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            onBaseRestoreInstanceState(savedInstanceState);
        } else if (intent != null) {
            onBaseRestoreInstanceState(intent);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.item_stat);
        TextView empty = (TextView) findViewById(R.id.empty);
        Plot2d graph = (Plot2d) findViewById(R.id.plot);
        float[] data = DataHistory.get(mEX);
        if (data.length > 0) {
            Plot2d.ConfigBuilder cfg = new Plot2d.ConfigBuilder().setBackroundColor(Color.TRANSPARENT).setXAxisText(
                    getString(R.string.day));
            if (data.length < cfg.getNumSubAxis()) {
                cfg.setNumSubAxis(data.length);
            }
            graph.init(data, cfg);
            graph.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            TextView sub_title = (TextView) findViewById(R.id.sub_title);
            int total = (int) DataHistory.getSumTime(mEX);
            sub_title.setText(getString(Utils.numEnding(total), String.valueOf(total)));
        } else {
            graph.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            TextView sub_title = (TextView) findViewById(R.id.sub_title);
            sub_title.setText(getString(Utils.numEnding(0), String.valueOf(0)));
        }

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(mEX.string);
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
