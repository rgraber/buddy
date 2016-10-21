package com.example.rebeccagraber.buddy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rebeccagraber on 10/20/16.
 */

public class FacebookEvent implements Parcelable {
    private int ID;
    private String name;
    public FacebookEvent(int nID, String sname)
    {
        ID = nID;
        name = sname;

    }

    public FacebookEvent(Parcel in)
    {
        ID = in.readInt();
        name = in.readString();
    }

    public static final Creator<FacebookEvent> CREATOR = new Creator<FacebookEvent>() {
        @Override
        public FacebookEvent createFromParcel(Parcel in) {
            return new FacebookEvent(in);
        }

        @Override
        public FacebookEvent[] newArray(int size) {
            return new FacebookEvent[size];
        }
    };

    public int getID()
    {
        return ID;
    }




    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(name);
    }
}
