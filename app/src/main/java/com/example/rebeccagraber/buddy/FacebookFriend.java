package com.example.rebeccagraber.buddy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rebeccagraber on 10/23/16.
 */

public class FacebookFriend implements Parcelable {
    int id;
    String name;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public FacebookFriend(int i, String s)
    {
        id = i;
        name = s;
    }

    public FacebookFriend(Parcel in)
    {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<FacebookFriend> CREATOR = new Creator<FacebookFriend>() {
        @Override
        public FacebookFriend createFromParcel(Parcel in) {
            return new FacebookFriend(in);
        }

        @Override
        public FacebookFriend[] newArray(int size) {
            return new FacebookFriend[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
