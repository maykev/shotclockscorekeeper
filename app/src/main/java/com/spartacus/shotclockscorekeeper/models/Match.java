package com.spartacus.shotclockscorekeeper.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Match implements Parcelable {

    // JSON keys
    private static final String ID = "id";
    private static final String TABLE_NUMBER = "table_number";
    private static final String PLAYERS = "players";

    private final String id;
    private final Player playerOne;
    private final Player playerTwo;
    private final int tableNumber;

    public Match(String id, int tableNumber, Player playerOne, Player playerTwo) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public static Match fromJson(JSONObject matchJson) throws JSONException {
        String id = matchJson.getString(ID);
        int tableNumber = matchJson.optInt(TABLE_NUMBER);
        JSONArray playersJson = matchJson.getJSONArray(PLAYERS);
        Player playerOne = Player.fromJson(playersJson.getJSONObject(0));
        Player playerTwo = Player.fromJson(playersJson.getJSONObject(1));


        return new Match(id, tableNumber, playerOne, playerTwo);
    }

    public String getId() {
        return id;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(tableNumber);
        dest.writeParcelable(playerOne, 0);
        dest.writeParcelable(playerTwo, 0);
    }

    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        public Match createFromParcel(Parcel in) {
            String id = in.readString();
            int tableNumber = in.readInt();
            Player playerOne = in.readParcelable(Player.class.getClassLoader());
            Player playerTwo = in.readParcelable(Player.class.getClassLoader());

            return new Match(id, tableNumber, playerOne, playerTwo);
        }

        public Match[] newArray(int size) {
            return new Match[size];
        }
    };


}
