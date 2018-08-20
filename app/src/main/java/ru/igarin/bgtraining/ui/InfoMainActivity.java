package ru.igarin.bgtraining.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import ru.igarin.bgtraining.Const;
import ru.igarin.bgtraining.R;
import ru.igarin.bgtraining.StaticInfo;
import ru.igarin.bgtraining.utils.Utils;

public class InfoMainActivity extends ActionBarActivity {

    public static final String INTENT_EXTRA_INFO = "INTENT_EXTRA_INFO";
    private StaticInfo info = new StaticInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_info_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            onBaseRestoreInstanceState(savedInstanceState);
        } else if (intent != null) {
            onBaseRestoreInstanceState(intent);
        }

        TextView info_textView = (TextView) this.findViewById(R.id.info_textView);
        info_textView.setText(Html.fromHtml(Const.getFile(this, info.file)));

        TextView info_add_textView = (TextView) this.findViewById(R.id.info_add_textView);
        info_add_textView.setText(Utils.getWordsInfo(this));

        findViewById(R.id.visit_website).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.openUrl(InfoMainActivity.this, "http://bgtraining.ru/");
            }

        });

        if (info.showAddInfo) {
            findViewById(R.id.info_add_container).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.info_add_container).setVisibility(View.GONE);
        }

        this.setTitle(info.title);

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        onBaseSaveInstanceState(outState);
    }

    public void onBaseSaveInstanceState(final Bundle outState) {
        outState.putParcelable(INTENT_EXTRA_INFO, info);
    }

    ;

    public void onBaseRestoreInstanceState(final Bundle outState) {
        info = outState.getParcelable(INTENT_EXTRA_INFO);
    }

    ;

    public void onBaseRestoreInstanceState(final Intent intent) {
        info = intent.getParcelableExtra(INTENT_EXTRA_INFO);
    }

}
