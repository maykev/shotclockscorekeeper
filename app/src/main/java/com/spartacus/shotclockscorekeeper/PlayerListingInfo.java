package com.spartacus.shotclockscorekeeper;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerListingInfo implements Parcelable {
    private final String id;
    private final String fullName;

    public PlayerListingInfo(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public static PlayerListingInfo fromJson(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString("id");
        String fullName = jsonObject.getString("full_name");

        return new PlayerListingInfo(id, fullName);
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullName);
    }

    public static final Parcelable.Creator<PlayerListingInfo> CREATOR = new Parcelable.Creator<PlayerListingInfo>() {
        public PlayerListingInfo createFromParcel(Parcel in) {
            String id = in.readString();
            String fullName = in.readString();

            return new PlayerListingInfo(id, fullName);
        }

        public PlayerListingInfo[] newArray(int size) {
            return new PlayerListingInfo[size];
        }
    };
}
