package ru.igarin.bgtraining.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.igarin.bgtraining.EX;
import ru.igarin.bgtraining.utils.L;

@DatabaseTable
public class DataHistory {

    public static final String COLUMN_ID = "timastamp";
    public static final String COLUMN_EX = "ex";

    @DatabaseField(id = true, columnName = COLUMN_ID)
    public long timestamp;

    @DatabaseField(columnName = COLUMN_EX)
    public String ex;

    @DatabaseField
    public long milliseconds;

    private static synchronized void nurseDataBase(EX ex) {
        List<DataHistory> history = new ArrayList<DataHistory>();
        try {
            history = HelperFactory.getHelper().getDataHistoryDao().queryBuilder().orderBy(COLUMN_ID, true).where()
                    .eq(COLUMN_EX, ex.name()).query();
        } catch (SQLException e) {
            L.e(e);
        }
        boolean needNurse = false;
        for (DataHistory h1 : history) {
            for (DataHistory h2 : history) {
                if (isSameDay(h1.timestamp, h2.timestamp) && h1.timestamp != h2.timestamp) {
                    needNurse = true;
                }
            }
        }
        if (needNurse) {
            List<DataHistory> newHistory = new ArrayList<DataHistory>();
            for (DataHistory H : history) {
                boolean added = false;
                for (DataHistory newH : newHistory) {
                    if (isSameDay(newH.timestamp, H.timestamp)) {
                        newH.milliseconds += H.milliseconds;
                        added = true;
                        newH.update();
                        continue;
                    }
                }
                if (!added) {
                    DataHistory new_h = DataHistory.get(ex, H.timestamp);
                    new_h.milliseconds = H.milliseconds;
                    new_h.update();
                    newHistory.add(new_h);
                }
                DataHistory.delete(H);
            }
        }
    }

    private static synchronized void merge(EX ex) {
        nurseDataBase(ex);
        List<DataRecord> records = DataRecord.get(ex);
        List<DataHistory> history = new ArrayList<DataHistory>();
        try {
            history = HelperFactory.getHelper().getDataHistoryDao().queryBuilder().orderBy(COLUMN_ID, true).where()
                    .eq(COLUMN_EX, ex.name()).query();
        } catch (SQLException e) {
            L.e(e);
        }

        for (DataRecord r : records) {
            boolean added = false;
            for (DataHistory h : history) {
                if (isSameDay(h.timestamp, r.timestamp)) {
                    h.milliseconds += r.milliseconds;
                    added = true;
                    h.update();
                    continue;
                }
            }
            if (!added) {
                DataHistory new_h = DataHistory.get(ex, r.timestamp);
                new_h.milliseconds = r.milliseconds;
                new_h.update();
                history.add(new_h);
            }
            DataRecord.delete(r);
        }
    }

    public static float[] get(EX ex) {
        merge(ex);
        List<DataHistory> data = new ArrayList<DataHistory>();
        try {
            data = HelperFactory.getHelper().getDataHistoryDao().queryBuilder().orderBy(COLUMN_ID, true).where()
                    .eq(COLUMN_EX, ex.name()).query();
        } catch (SQLException e) {
            L.e(e);
        }

        float[] ret = new float[data.size()];
        for (int i = 0; i < data.size(); i++) {
            float temp = data.get(i).milliseconds / (60000);
            temp = new BigDecimal(temp).setScale(0, RoundingMode.HALF_EVEN).intValue();
            ret[i] = temp > 0 ? temp : 1;
        }

        return ret;
    }

    public static void delete(DataHistory obj) {
        try {
            DeleteBuilder<DataHistory, Integer> deleteBuilder = HelperFactory.getHelper().getDataHistoryDao()
                    .deleteBuilder();
            deleteBuilder.where().eq(COLUMN_ID, obj.timestamp);
            deleteBuilder.delete();
        } catch (SQLException e) {
            L.e(e);
        }
    }

    private static boolean isSameDay(long firstTime, long secondTime) {
        Date date1 = new Date(firstTime);
        Date date2 = new Date(secondTime);
        return isSameDay(date1, date2);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
                .get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static DataHistory get(EX ex, long time) {
        List<DataHistory> data = new ArrayList<DataHistory>();
        try {
            data = HelperFactory.getHelper().getDataHistoryDao().queryBuilder().where().eq(COLUMN_ID, time).query();
        } catch (SQLException e) {
            L.e(e);
        }
        if (data.isEmpty()) {
            DataHistory ret = new DataHistory();
            ret.timestamp = time;
            ret.ex = ex.name();
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

    public static void update(DataHistory obj) {
        try {
            HelperFactory.getHelper().getDataHistoryDao().createOrUpdate(obj);
        } catch (SQLException e) {
            L.e(e);
        }
    }

    public static void clearTable() {
        try {
            TableUtils.clearTable(HelperFactory.getHelper().getDataHistoryDao().getConnectionSource(),
                    DataHistory.class);
        } catch (SQLException e) {
            L.e(e);
        }
    }

    public static float getSumTime(EX ex) {
        float[] list = get(ex);
        float sum = 0;
        for (float item : list) {
            sum += item;
        }
        return sum;
    }

    public static String getDebugInfo(EX ex) {
        merge(ex);
        List<DataHistory> data = new ArrayList<DataHistory>();
        try {
            data = HelperFactory.getHelper().getDataHistoryDao().queryBuilder().orderBy(COLUMN_ID, true).where()
                    .eq(COLUMN_EX, ex.name()).query();
        } catch (SQLException e) {
            L.e(e);
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < data.size(); i++) {
            sb.append(getTime(data.get(i).timestamp));
            sb.append(" - ");

            float temp = data.get(i).milliseconds / (60000);
            temp = new BigDecimal(temp).setScale(0, RoundingMode.HALF_EVEN).intValue();
            sb.append(temp > 0 ? temp : 1);
            sb.append("\n");
        }

        return sb.toString();
    }

    private static String getTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        return sdf.format(c.getTime());
    }

}
