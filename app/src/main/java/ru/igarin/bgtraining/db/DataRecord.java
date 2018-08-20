package ru.igarin.bgtraining.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.igarin.bgtraining.EX;
import ru.igarin.bgtraining.utils.L;

@DatabaseTable
public class DataRecord {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EX = "ex";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    @DatabaseField(id = true, columnName = COLUMN_ID)
    public String id;

    @DatabaseField(columnName = COLUMN_EX)
    public String ex;

    @DatabaseField(columnName = COLUMN_TIMESTAMP)
    public long timestamp;

    @DatabaseField
    public long milliseconds;

    public static List<DataRecord> get(EX ex) {
        List<DataRecord> data = new ArrayList<DataRecord>();
        try {
            data = HelperFactory.getHelper().getDataRecordDao().queryBuilder().orderBy(COLUMN_TIMESTAMP, true).where()
                    .eq(COLUMN_EX, ex.name()).query();
        } catch (SQLException e) {
            L.e(e);
        }
        return data;
    }

    public static DataRecord get(EX ex, long time) {
        String id = ex.name() + time;
        List<DataRecord> data = new ArrayList<DataRecord>();
        try {
            data = HelperFactory.getHelper().getDataRecordDao().queryBuilder().where().eq(COLUMN_ID, id).query();
        } catch (SQLException e) {
            L.e(e);
        }
        if (data.isEmpty()) {
            DataRecord ret = new DataRecord();
            ret.id = id;
            ret.ex = ex.name();
            ret.timestamp = time;
            return ret;
        } else if (data.size() > 1) {
            throw new RuntimeException();
        } else {
            return data.get(0);
        }
    }

    public void update() {
        update(this);
    }

    public static void update(DataRecord obj) {
        try {
            HelperFactory.getHelper().getDataRecordDao().createOrUpdate(obj);
        } catch (SQLException e) {
            L.e(e);
        }
    }

    public static void delete(DataRecord obj) {
        try {
            DeleteBuilder<DataRecord, Integer> deleteBuilder = HelperFactory.getHelper().getDataRecordDao()
                    .deleteBuilder();
            deleteBuilder.where().eq(COLUMN_ID, obj.id);
            deleteBuilder.delete();
        } catch (SQLException e) {
            L.e(e);
        }
    }

    public static void clearTable() {
        try {
            TableUtils.clearTable(HelperFactory.getHelper().getDataRecordDao().getConnectionSource(),
                    DataRecord.class);
        } catch (SQLException e) {
            L.e(e);
        }
    }
}
