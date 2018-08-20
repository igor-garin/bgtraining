package ru.igarin.bgtraining.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.lang.ref.WeakReference;

import io.fabric.sdk.android.Fabric;
import ru.igarin.bgtraining.Const;
import ru.igarin.bgtraining.EX;
import ru.igarin.bgtraining.R;
import ru.igarin.bgtraining.StaticInfo;
import ru.igarin.bgtraining.db.DataHistory;
import ru.igarin.bgtraining.db.DataRecord;
import ru.igarin.bgtraining.db.DataReminder;
import ru.igarin.bgtraining.db.DataWord;
import ru.igarin.bgtraining.utils.AbsListViewAdapter;
import ru.igarin.bgtraining.utils.AnimationUtils;
import ru.igarin.bgtraining.utils.SharedPrefer;
import ru.igarin.bgtraining.utils.Utils;

public class MainActivity extends ActionBarActivity {

    private ProgressDialog mProgressDialog;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main_screen);

        TasksAdapter tasksAdapter = new TasksAdapter(this);
        tasksAdapter.updateList(EX.values());
        mListView = (ListView) findViewById(R.id.list);
        mListView.setDividerHeight(0);
        mListView.setAdapter(tasksAdapter);

        if (!SharedPrefer.getInstance().contains(SharedPrefer.SP_RUN) || Utils.getMinWordsCount(this) == 0) {
            new InitTask().execute();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rules: {
                Intent intent = new Intent(this, InfoMainActivity.class);
                intent.putExtra(InfoMainActivity.INTENT_EXTRA_INFO, new StaticInfo(Const.INFO_FILE_RULES, R.string.rules));
                startActivity(intent);

                return true;
            }
            case R.id.reset: {

                DataHistory.clearTable();
                DataRecord.clearTable();
                DataReminder.clearTable();

                return true;
            }
            case R.id.about: {
                Intent intent = new Intent(this, InfoMainActivity.class);
                StaticInfo stInfo = new StaticInfo(Const.INFO_FILE_ABOUT, R.string.about);
                stInfo.showAddInfo = true;
                intent.putExtra(InfoMainActivity.INTENT_EXTRA_INFO, stInfo);
                startActivity(intent);

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private static class TasksHolder {
        public View view;
        public ViewGroup parent;
        public TextView title;
        public View viewButtons;
        public View viewGo;
        public View viewStat;
        public View viewReminder;

        private static WeakReference<View> current;
        private static final Object lock = new Object();

        protected static void close(final TasksHolder holder) {
            if (Utils.isVisible(holder.viewButtons)) {
                Utils.setVisibility(holder.viewButtons, false);
                AnimationUtils.setHeight(holder.viewButtons, 0);
                synchronized (lock) {
                    current = null;
                }
            }
        }

        private static void toggle(final TasksHolder holder) {
            if (Utils.isVisible(holder.viewButtons)) {
                AnimationUtils.collapse(holder.viewButtons);
                synchronized (lock) {
                    current = null;
                }
            } else {
                synchronized (lock) {
                    if (current != null && current.get() != null) {
                        AnimationUtils.collapse(current.get());
                        current.clear();
                    }
                    current = new WeakReference<View>(holder.viewButtons);
                }
                AnimationUtils.expand(holder.viewButtons, holder.view, holder.parent);
            }
        }
    }

    private class TasksAdapter extends AbsListViewAdapter<EX, TasksHolder> {

        public TasksAdapter(Context context) {
            super(context);
        }

        @Override
        protected View createView(int position, ViewGroup parent) {
            return mLayoutInflater.inflate(R.layout.item_ex, null);
        }

        @Override
        protected TasksHolder createViewHolder(int position, View view, ViewGroup parent) {
            final TasksHolder holder = new TasksHolder();
            holder.view = view;
            holder.parent = parent;
            holder.title = (TextView) view.findViewById(R.id.item_title);
            holder.viewButtons = view.findViewById(R.id.item_buttons);
            holder.viewGo = view.findViewById(R.id.item_go);
            holder.viewStat = view.findViewById(R.id.item_stat);
            holder.viewReminder = view.findViewById(R.id.item_reminder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TasksHolder.toggle(holder);

                }
            });
            TasksHolder.close(holder);
            return holder;
        }

        @Override
        protected void bindView(int position, View view, ViewGroup parent, final TasksHolder holder) {
            final EX item = getItem(position);
            holder.title.setText(item.string);

            holder.viewGo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(MainActivity.this, ExersiseActivty.class);
                    intent.putExtra(ExersiseActivty.INTENT_EXTRA_TYPE, item.ordinal());
                    startActivity(intent);

                }

            });

            holder.viewStat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(MainActivity.this, StatActivity.class);
                    intent.putExtra(StatActivity.INTENT_EXTRA_TYPE, item.ordinal());
                    startActivity(intent);

                }

            });

            holder.viewReminder.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                    intent.putExtra(ReminderActivity.INTENT_EXTRA_TYPE, item.ordinal());
                    startActivity(intent);

                }

            });

        }

    }

    private class InitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle(null);
            mProgressDialog.setMessage(getString(R.string.data_preparing));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DataWord.fillData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            Intent intent = new Intent(MainActivity.this, InfoMainActivity.class);
            StaticInfo stInfo = new StaticInfo(Const.INFO_FILE_ABOUT, R.string.about);
            stInfo.showAddInfo = true;
            intent.putExtra(InfoMainActivity.INTENT_EXTRA_INFO, stInfo);
            startActivity(intent);
            SharedPrefer.getInstance().putBoolean(SharedPrefer.SP_RUN, true);

        }

    }

}
