package ru.igarin.bgtraining.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import ru.igarin.bgtraining.Const;
import ru.igarin.bgtraining.R;
import ru.igarin.bgtraining.StaticInfo;

public class InfoExActivity extends ActionBarActivity {

    public static final String INTENT_EXTRA_INFO = "INTENT_EXTRA_INFO";
    public static final String INTENT_EXTRA_SHOW_BUTTON = "INTENT_EXTRA_SHOW_BUTTON";

    private StaticInfo info = new StaticInfo();
    private boolean showButton = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_info_ex);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            onBaseRestoreInstanceState(savedInstanceState);
        } else if (intent != null) {
            onBaseRestoreInstanceState(intent);
        }

        TextView info_textView = (TextView) this.findViewById(R.id.info_textView);
        info_textView.setText(Html.fromHtml(Const.getFile(this, info.file)));
        this.setTitle(info.title);

        Button buttonNext = (Button) this.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        if (!showButton) {
            buttonNext.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        onBaseSaveInstanceState(outState);
    }

    public void onBaseSaveInstanceState(final Bundle outState) {
        outState.putParcelable(INTENT_EXTRA_INFO, info);
        outState.putBoolean(INTENT_EXTRA_SHOW_BUTTON, showButton);
    }

    ;

    public void onBaseRestoreInstanceState(final Bundle outState) {
        info = outState.getParcelable(INTENT_EXTRA_INFO);
        showButton = outState.getBoolean(INTENT_EXTRA_SHOW_BUTTON);
    }

    ;

    public void onBaseRestoreInstanceState(final Intent intent) {
        info = intent.getParcelableExtra(INTENT_EXTRA_INFO);
        showButton = intent.getBooleanExtra(INTENT_EXTRA_SHOW_BUTTON, showButton);
    }

}
