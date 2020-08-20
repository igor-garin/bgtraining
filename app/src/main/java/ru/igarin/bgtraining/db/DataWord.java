package ru.igarin.bgtraining.db;

import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ru.igarin.bgtraining.App;
import ru.igarin.bgtraining.R;
import ru.igarin.bgtraining.utils.L;

@DatabaseTable
public class DataWord {

    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_TYPE = "type";

    @DatabaseField(id = true)
    public int _id;

    @DatabaseField(columnName = COLUMN_TYPE)
    public String type;

    @DatabaseField
    public String word;

    @DatabaseField(columnName = COLUMN_NUMBER)
    public int number;

    @DatabaseField
    public int usecount;

    public static DataWord get(int number, String type) {
        List<DataWord> dataWord = new ArrayList<DataWord>();
        try {
            dataWord = HelperFactory.getHelper().getDataWordDao().queryBuilder().where()
                    .eq(DataWord.COLUMN_NUMBER, number).and().eq(DataWord.COLUMN_TYPE, type).query();
        } catch (SQLException e) {
            L.e(e);
        }
        if (dataWord.isEmpty()) {
            return null;
        } else if (dataWord.size() > 1) {
            throw new RuntimeException();
        } else {
            return dataWord.get(0);
        }
    }

    public static long getCount(String type) {
        long numRows = 0;
        try {
            numRows = HelperFactory.getHelper().getDataWordDao().queryBuilder().where().eq(DataWord.COLUMN_TYPE, type)
                    .countOf();
        } catch (SQLException e) {
            L.e(e);
        }
        return numRows;
    }

    public void update() {
        update(this);
    }

    public static void update(DataWord obj) {
        try {
            HelperFactory.getHelper().getDataWordDao().update(obj);
        } catch (SQLException e) {
            L.e(e);
        }
    }

    public static void fillData() {

        try {
            TableUtils.clearTable(HelperFactory.getHelper().getDataWordDao().getConnectionSource(), DataWord.class);

            final HelperDatabase databaseHelper = HelperFactory.getHelper();
            final SQLiteDatabase database = databaseHelper.getWritableDatabase();
            if (database != null) {
                TransactionManager.callInTransaction(databaseHelper.getConnectionSource(), new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        CharStreams.readLines(
                                new InputStreamReader(App.getInstance().getResources().openRawResource(R.raw.sql_dump),
                                        Charsets.UTF_8), new LineProcessor<Object>() {
                                    @Override
                                    public boolean processLine(String s) throws IOException {
                                        final String stmt = s.trim();
                                        if (stmt.length() > 0) {
                                            database.execSQL(stmt);
                                        }

                                        return true;
                                    }

                                    @Override
                                    public Object getResult() {
                                        return null;
                                    }
                                });

                        return null;
                    }
                });
            }
        } catch (SQLException e) {
            L.e(e);
        }
    }

}
