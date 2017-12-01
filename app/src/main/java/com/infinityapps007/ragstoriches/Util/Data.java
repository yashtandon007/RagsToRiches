package com.infinityapps007.ragstoriches.Util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Magic Lenz on 01-Nov-17.
 */

public class Data implements Parcelable {
    public String person_icon_pathname;
    public String person_heading;
    public String person_category;
    public String person_name;
    public String timestamp;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.person_icon_pathname);
        dest.writeString(this.person_heading);
        dest.writeString(this.person_category);
        dest.writeString(this.person_name);
        dest.writeString(this.timestamp);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.person_icon_pathname = in.readString();
        this.person_heading = in.readString();
        this.person_category = in.readString();
        this.person_name = in.readString();
        this.timestamp = in.readString();
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
