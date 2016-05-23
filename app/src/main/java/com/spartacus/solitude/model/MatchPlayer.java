package com.spartacus.solitude.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MatchPlayer extends Player implements Parcelable {
    @SerializedName("games_on_the_wire")
    private int gamesOnTheWire;

    @SerializedName("display_name")
    private String displayName;

    protected MatchPlayer(Parcel in) {
        super(in);
        gamesOnTheWire = in.readInt();
        displayName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(gamesOnTheWire);
        dest.writeString(displayName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchPlayer> CREATOR = new Creator<MatchPlayer>() {
        @Override
        public MatchPlayer createFromParcel(Parcel in) {
            return new MatchPlayer(in);
        }

        @Override
        public MatchPlayer[] newArray(int size) {
            return new MatchPlayer[size];
        }
    };

    public int getGamesOnTheWire() {
        return gamesOnTheWire;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchPlayer)) return false;
        if (!super.equals(o)) return false;

        MatchPlayer that = (MatchPlayer) o;

        if (gamesOnTheWire != that.gamesOnTheWire) return false;
        return !(displayName != null ? !displayName.equals(that.displayName) : that.displayName != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + gamesOnTheWire;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MatchPlayer{" +
                "gamesOnTheWire=" + gamesOnTheWire +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
