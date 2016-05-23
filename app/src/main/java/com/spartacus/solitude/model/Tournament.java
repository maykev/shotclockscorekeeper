package com.spartacus.solitude.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tournament implements Parcelable {


    private static final String MODE_FULLY_CONNECTED = "full";

    @SerializedName("id")
    private int id;

    @SerializedName("mode")
    private String mode;

    @SerializedName("name")
    private String name;

    @SerializedName("tables")
    private List<Integer> tables;

    Tournament() {
        // GSON
    }

    protected Tournament(Parcel in) {
        id = in.readInt();
        mode = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mode);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tournament> CREATOR = new Creator<Tournament>() {
        @Override
        public Tournament createFromParcel(Parcel in) {
            return new Tournament(in);
        }

        @Override
        public Tournament[] newArray(int size) {
            return new Tournament[size];
        }
    };

    public List<Integer> getTables() {
        return tables;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isFullyConnected() {
        return MODE_FULLY_CONNECTED.equals(mode);
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "mode='" + mode + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tables=" + tables +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tournament)) return false;

        Tournament that = (Tournament) o;

        if (id != that.id) return false;
        if (mode != null ? !mode.equals(that.mode) : that.mode != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(tables != null ? !tables.equals(that.tables) : that.tables != null);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tables != null ? tables.hashCode() : 0);
        return result;
    }


}
