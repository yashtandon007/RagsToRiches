package com.infinityapps007.ragstoriches.Util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Magic Lenz on 01-Nov-17.
 */

public class Data_DetailActivity implements Parcelable {
    public String person_icon_pathname;
    public String person_heading;
    public String person_category;
    public String person_name;
    public String id;
    public String person_story;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.person_icon_pathname);
        dest.writeString(this.id);
        dest.writeString(this.person_heading);
        dest.writeString(this.person_category);
        dest.writeString(this.person_name);
        dest.writeString(this.person_story);
    }

    public Data_DetailActivity() {
    }

    protected Data_DetailActivity(Parcel in) {
        this.person_icon_pathname = in.readString();
        this.id = in.readString();
        this.person_heading = in.readString();
        this.person_category = in.readString();
        this.person_name = in.readString();
        this.person_story = in.readString();
    }

    public static final Creator<Data_DetailActivity> CREATOR = new Creator<Data_DetailActivity>() {
        @Override
        public Data_DetailActivity createFromParcel(Parcel source) {
            return new Data_DetailActivity(source);
        }

        @Override
        public Data_DetailActivity[] newArray(int size) {
            return new Data_DetailActivity[size];
        }
    };
}
