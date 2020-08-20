package ru.igarin.bgtraining.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.igarin.bgtraining.utils.L;

public class HelperDatabase extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "bgtraining.db";
    private static final int DATABASE_VERSION = 7;

    public HelperDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DataWord.class);
            TableUtils.createTable(connectionSource, DataReminder.class);
            TableUtils.createTable(connectionSource, DataRecord.class);
            TableUtils.createTable(connectionSource, DataHistory.class);
        } catch (SQLException e) {
            L.e("error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, DataWord.class, true);
            TableUtils.dropTable(connectionSource, DataReminder.class, true);
            TableUtils.dropTable(connectionSource, DataRecord.class, true);
            TableUtils.dropTable(connectionSource, DataHistory.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            L.e("error upgrading db " + DATABASE_NAME + "from ver " + oldVersion);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
    }

    private Dao<DataWord, Integer> dataWordDao = null;

    public Dao<DataWord, Integer> getDataWordDao() {
        if (null == dataWordDao) {
            try {
                dataWordDao = getDao(DataWord.class);
            } catch (java.sql.SQLException e) {
                L.e(e);
            }
        }
        return dataWordDao;
    }

    private Dao<DataReminder, Integer> dataReminderDao = null;

    public Dao<DataReminder, Integer> getDataReminderDao() {
        if (null == dataReminderDao) {
            try {
                dataReminderDao = getDao(DataReminder.class);
            } catch (java.sql.SQLException e) {
                L.e(e);
            }
        }
        return dataReminderDao;
    }

    private Dao<DataRecord, Integer> dataRecordDao = null;

    public Dao<DataRecord, Integer> getDataRecordDao() {
        if (null == dataRecordDao) {
            try {
                dataRecordDao = getDao(DataRecord.class);
            } catch (java.sql.SQLException e) {
                L.e(e);
            }
        }
        return dataRecordDao;
    }


    private Dao<DataHistory, Integer> dataHistoryDao = null;

    public Dao<DataHistory, Integer> getDataHistoryDao() {
        if (null == dataHistoryDao) {
            try {
                dataHistoryDao = getDao(DataHistory.class);
            } catch (java.sql.SQLException e) {
                L.e(e);
            }
        }
        return dataHistoryDao;
    }
}