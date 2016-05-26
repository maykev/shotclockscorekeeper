package com.spartacus.solitude.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Match implements Parcelable {

    public static final String STATUS_FINISHED = "finished";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_CREATED = "created";

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    @SerializedName("id")
    private int id;

    @SerializedName("race")
    private int race;

    @SerializedName("tournament_id")
    private int tournamentId;

    @SerializedName("players")
    private List<MatchPlayer> players;

    @SerializedName("table_number")
    private Integer table;

    @SerializedName("status")
    private String status;

    protected Match(Parcel in) {
        id = in.readInt();
        tournamentId = in.readInt();
        race = in.readInt();
        status = in.readString();

        players = new ArrayList<>();
        in.readTypedList(players, MatchPlayer.CREATOR);

        if (in.readByte() == 1) {
            table = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(tournamentId);
        dest.writeInt(race);
        dest.writeString(status);
        dest.writeTypedList(players);

        if (table != null) {
            dest.writeByte((byte) 1);
            dest.writeInt(table);
        } else {
            dest.writeByte((byte) 0);
        }
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", race=" + race +
                ", tournamentId=" + tournamentId +
                ", players=" + players +
                ", table=" + table +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;

        Match match = (Match) o;

        if (id != match.id) return false;
        if (race != match.race) return false;
        if (tournamentId != match.tournamentId) return false;
        if (players != null ? !players.equals(match.players) : match.players != null) return false;
        if (table != null ? !table.equals(match.table) : match.table != null) return false;
        return status != null ? status.equals(match.status) : match.status == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + race;
        result = 31 * result + tournamentId;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        result = 31 * result + (table != null ? table.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public int getRace() {
        return race;
    }

    public List<MatchPlayer> getPlayers() {
        return players;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public MatchPlayer getPlayerOne() {
        return getPlayers().get(0);
    }

    public MatchPlayer getPlayerTwo() {
        return getPlayers().get(1);
    }

    public Integer getTable() {
        return table;
    }

    public String getStatus() {
        return status;
    }
}
