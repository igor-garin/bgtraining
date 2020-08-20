package ru.igarin.bgtraining.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.igarin.bgtraining.EX;
import ru.igarin.bgtraining.utils.L;

@DatabaseTable
public class DataReminder {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EX = "ex";

    @DatabaseField(id = true, columnName = COLUMN_ID)
    public String id;

    @DatabaseField(columnName = COLUMN_EX)
    public String ex;

    @DatabaseField
    public int day;

    @DatabaseField
    public int hourOfDay;

    @DatabaseField
    public int minute;

    @DatabaseField
    public boolean active;

    public static long triggerAtMillis(DataReminder reminder) {
        Calendar c = Calendar.getInstance();
        switch (reminder.day) {
            case 0:
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case 1:
                c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case 2:
                c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case 3:
                c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case 4:
                c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case 5:
                c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
            case 6:
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
        }

        Calendar ret_c = Calendar.getInstance();
        ret_c.clear();
        ret_c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), reminder.hourOfDay,
                reminder.minute);
        if (Calendar.getInstance().after(ret_c)) {
            ret_c.add(Calendar.DATE, 7);
        }
        return ret_c.getTimeInMillis();
    }

    public static List<DataReminder> get() {
        List<DataReminder> data = new ArrayList<DataReminder>();
        try {
            data = HelperFactory.getHelper().getDataReminderDao().queryBuilder().query();
        } catch (SQLException e) {
            L.e(e);
        }
        return data;
    }

    public static List<DataReminder> get(EX ex) {
        List<DataReminder> data = new ArrayList<DataReminder>();
        try {
            data = HelperFactory.getHelper().getDataReminderDao().queryBuilder().where()
                    .eq(DataReminder.COLUMN_EX, ex.name()).query();
        } catch (SQLException e) {
            L.e(e);
        }
        return data;
    }

    public static DataReminder get(EX ex, int day) {
        String id = ex.name() + day;
        List<DataReminder> data = new ArrayList<DataReminder>();
        try {
            data = HelperFactory.getHelper().getDataReminderDao().queryBuilder().where().eq(DataReminder.COLUMN_ID, id)
                    .query();
        } catch (SQLException e) {
            L.e(e);
        }
        if (data.isEmpty()) {
            DataReminder ret = new DataReminder();
            ret.id = id;
            ret.ex = ex.name();
            ret.day = day;
            ret.hourOfDay = 11;
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

    public static void update(DataReminder obj) {
        try {
            HelperFactory.getHelper().getDataReminderDao().createOrUpdate(obj);
        } catch (SQLException e) {
            L.e(e);
        }
    }

    public static void clearTable() {
        try {
            TableUtils.clearTable(HelperFactory.getHelper().getDataReminderDao().getConnectionSource(),
                    DataReminder.class);
        } catch (SQLException e) {
            L.e(e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataReminder other = (DataReminder) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
