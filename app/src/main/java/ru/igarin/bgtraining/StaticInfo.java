package ru.igarin.bgtraining;

import android.os.Parcel;
import android.os.Parcelable;

public class StaticInfo implements Parcelable {

    public String file;
    public int title;
    public boolean showAddInfo = false;
    public String ex;

    public StaticInfo() {
        file = Const.INFO_FILE_ABOUT;
        title = R.string.about;
    }

    public StaticInfo(String file, int title) {
        this.file = file;
        this.title = title;
    }

    protected StaticInfo(Parcel in) {
        file = in.readString();
        title = in.readInt();
        showAddInfo = in.readInt() == 1;
        ex = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(file);
        dest.writeInt(title);
        dest.writeInt(showAddInfo ? 1 : 0);
        dest.writeString(ex);
    }

    public static final Parcelable.Creator<StaticInfo> CREATOR = new Parcelable.Creator<StaticInfo>() {
        @Override
        public StaticInfo createFromParcel(Parcel in) {
            return new StaticInfo(in);
        }

        @Override
        public StaticInfo[] newArray(int size) {
            return new StaticInfo[size];
        }
    };
}
